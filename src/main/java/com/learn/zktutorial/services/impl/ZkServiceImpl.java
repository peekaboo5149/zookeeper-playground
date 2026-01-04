package com.learn.zktutorial.services.impl;

import com.learn.zktutorial.dto.request.ZkUpdateRequest;
import com.learn.zktutorial.dto.response.ZkChildrenResponse;
import com.learn.zktutorial.dto.response.ZkNodeDataResponse;
import com.learn.zktutorial.dto.response.ZkOperationResponse;
import com.learn.zktutorial.dto.response.ZkSequentialResponse;
import com.learn.zktutorial.services.ZkService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.data.Stat;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class ZkServiceImpl implements ZkService {

    private final CuratorFramework curatorFramework;

    @Override
    public ZkOperationResponse createPersistentNode(String path, String data) {

        try {
            if (doesExist(path)) {
                return new ZkOperationResponse(
                        false,
                        path,
                        "Persistent znode already exists"
                );
            }

            curatorFramework.create()
                    .creatingParentsIfNeeded()
                    .withMode(CreateMode.PERSISTENT)
                    .forPath(path, data.getBytes());

            return new ZkOperationResponse(
                    true,
                    path,
                    "Persistent znode created successfully"
            );
        } catch (Exception e) {
            log.error("Error creating persistent znode at path: {}", path, e);
            throw new RuntimeException(
                    "Error creating persistent znode at path: " + path,
                    e
            );
        }

    }

    @Override
    public ZkOperationResponse createEphemeralNode(String path, String data) {
        try {
            if (doesExist(path)) {
                return new ZkOperationResponse(
                        false,
                        path,
                        "Ephemeral znode already exists"
                );
            }

            curatorFramework.create()
                    .creatingParentsIfNeeded()
                    .withMode(CreateMode.EPHEMERAL)
                    .forPath(path, data.getBytes());

            return new ZkOperationResponse(
                    true,
                    path,
                    "Empheral znode created successfully"
            );
        } catch (Exception e) {
            log.error("Error creating empheral znode at path: {}", path, e);
            throw new RuntimeException(
                    "Error creating empheral znode at path: " + path,
                    e
            );
        }
    }

    @Override
    public ZkNodeDataResponse getNodeData(String path) {
        try {
            Stat stat = new Stat();

            byte[] dataBytes = curatorFramework
                    .getData()
                    .storingStatIn(stat)
                    .forPath(path);

            String data = dataBytes != null
                    ? new String(dataBytes)
                    : null;

            return new ZkNodeDataResponse(
                    path,
                    data,
                    stat.getVersion()
            );

        } catch (Exception e) {
            log.error("Failed to read znode data for path: {}", path, e);
            throw new RuntimeException(
                    "Failed to read znode data for path: " + path,
                    e
            );
        }
    }


    @Override
    public ZkChildrenResponse getChildren(String path) {
        try {
            Stat stat = new Stat();
            List<String> childrens = curatorFramework.getChildren()
                    .storingStatIn(stat)
                    .forPath(path);
            return new ZkChildrenResponse(path, childrens);
        } catch (Exception e) {
            log.error("Failed to get children for path: {}", path, e);
            throw new RuntimeException("Failed to read znode children for path: " + path, e);
        }
    }

    @Override
    public ZkSequentialResponse createSequentialNode(String path) {
        try {
            String createdPath = curatorFramework.create()
                    .creatingParentsIfNeeded()
                    .withMode(CreateMode.PERSISTENT_SEQUENTIAL)
                    .forPath(path);

            return new ZkSequentialResponse(
                    path,
                    createdPath
            );

        } catch (Exception e) {
            throw new RuntimeException(
                    "Failed to create sequential znode for path: " + path,
                    e
            );
        }
    }

    @Override
    public ZkOperationResponse deleteNode(String path) {
        try {
            if (!doesExist(path))
                return new ZkOperationResponse(false, path, "Znode does not exist");
            curatorFramework.delete().forPath(path);
            return new ZkOperationResponse(true, path, "Znode deleted successfully");
        } catch (Exception e) {
            log.error("Failed to delete children for path: {}", path, e);
            throw new RuntimeException("Failed to delete znode for path: " + path, e);
        }
    }

    @Override
    public ZkOperationResponse updateNode(String path, ZkUpdateRequest request) {
        try {
            curatorFramework.setData().withVersion(request.getExpectedVersion()).forPath(
                    path,
                    request.getData().getBytes()
            );
            return new ZkOperationResponse(
                    true,
                    path,
                    "Znode updated successfully"
            );

        } catch (KeeperException.BadVersionException e) {
            log.warn(
                    "Version conflict while updating znode {}. Expected version {}",
                    path,
                    request.getExpectedVersion()
            );
            return new ZkOperationResponse(
                    false,
                    path,
                    "Version conflict: znode was modified by another client"
            );
        } catch (KeeperException.NoNodeException e) {
            return new ZkOperationResponse(
                    false,
                    path,
                    "Znode does not exist"
            );
        } catch (Exception e) {
            log.error("Failed to update znode at path: {}", path, e);
            throw new RuntimeException(
                    "Failed to update znode for path: " + path,
                    e
            );
        }
    }

    private boolean doesExist(String path) throws Exception {
        return curatorFramework.checkExists().forPath(path) != null;
    }

}
