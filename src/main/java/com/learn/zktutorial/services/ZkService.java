package com.learn.zktutorial.services;

import com.learn.zktutorial.dto.request.ZkUpdateRequest;
import com.learn.zktutorial.dto.response.ZkChildrenResponse;
import com.learn.zktutorial.dto.response.ZkNodeDataResponse;
import com.learn.zktutorial.dto.response.ZkOperationResponse;
import com.learn.zktutorial.dto.response.ZkSequentialResponse;

public interface ZkService {
    /**
     * Create a persistent znode
     */
    ZkOperationResponse createPersistentNode(String path, String data);

    /**
     * Create an ephemeral znode
     */
    ZkOperationResponse createEphemeralNode(String path, String data);

    /**
     * Read znode data + version
     */
    ZkNodeDataResponse getNodeData(String path);

    /**
     * List children of a znode
     */
    ZkChildrenResponse getChildren(String path);

    /**
     * Create sequential znode
     */
    ZkSequentialResponse createSequentialNode(String path);

    /**
     * Delete a znode
     */
    ZkOperationResponse deleteNode(String path);

    /**
     * Update a znode
     */
    ZkOperationResponse updateNode(String path, ZkUpdateRequest request);
}
