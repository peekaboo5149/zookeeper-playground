package com.learn.zktutorial.services.impl;

import com.learn.zktutorial.services.ZkLeaderElectionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.Watcher;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
class ZkLeaderElectionServiceImpl implements ZkLeaderElectionService {

    private static final String ELECTION_PATH = "/election";
    private final CuratorFramework curatorFramework;
    private String currentNodePath;
    private volatile boolean leader = false;

    /*
     Rules
       - Every instance creates one ephemeral sequential node
       - Lowest sequence number = leader
       - Each node watches its predecessor
       - If predecessor disappears → re-check leadership
     */

    @Override
    public synchronized void participateInElection() {
        try {
            if (currentNodePath != null) {
                log.warn("Already participating in election with node path: {}", currentNodePath);
                return;
            }

            currentNodePath = curatorFramework.create()
                    .creatingParentsIfNeeded()
                    .withMode(CreateMode.EPHEMERAL_SEQUENTIAL)
                    .forPath(ELECTION_PATH + "/node-");
            log.info("Created election node: {}", currentNodePath);

            attemptLeadership();
        } catch (Exception e) {
            log.error("Failed to participate in election", e);
            throw new RuntimeException(e);
        }
    }

    private void attemptLeadership() throws Exception {
        List<String> children = curatorFramework.getChildren().forPath(ELECTION_PATH);
        Collections.sort(children);

        String nodeName = currentNodePath.substring(ELECTION_PATH.length() + 1);
        int index = children.indexOf(nodeName);
        if (index == 0) {
            leader = true;
            log.info("🎉 I AM THE LEADER ({})", currentNodePath);
            return;
        }

        leader = false;
        String predecessor = children.get(index - 1);
        String predecessorPath = ELECTION_PATH + "/" + predecessor;
        log.info("I am follower. Watching predecessor: {}", predecessorPath);

        curatorFramework.checkExists()
                .usingWatcher((Watcher) evnt -> {
                    if (evnt.getType() == Watcher.Event.EventType.NodeDeleted) {
                        try {
                            attemptLeadership();
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    }
                }).forPath(predecessorPath);
    }

    @Override
    public boolean isLeader() {
        return leader;
    }

    @Override
    public String getNodePath() {
        return currentNodePath;
    }
}
