package com.learn.zktutorial.services.impl;

import com.learn.zktutorial.helper.FileHelper;
import com.learn.zktutorial.services.ZkEventService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.data.Stat;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.time.Instant;

@Component
@RequiredArgsConstructor
@Slf4j
class ZkEventServiceImpl implements ZkEventService {

    private static final String EVENT_LOG_FILE = "logs/events.log";
    private static final String STATE_FILE = "data/zk/state.json";

    private final CuratorFramework curatorFramework;

    @PostConstruct
    void init() {
        FileHelper.createFileIfNotCreated(EVENT_LOG_FILE);
        FileHelper.createFileIfNotCreated(STATE_FILE);
    }

    @Override
    public void watchNodeData(String path) {
        try {
            Stat stat = new Stat();
            curatorFramework.getData()
                    .storingStatIn(stat)
                    .usingWatcher((Watcher) event -> {
                        if (event.getType() == Watcher.Event.EventType.NodeDataChanged) {
                            log.info("ZNode data changed at path: {}", event.getPath());
                            appendEventLog(event.getPath());
                            persistLatestState(event.getPath());
                            watchNodeData(path);
                        }
                    }).forPath(path);
            log.info("Watch registered for znode data at path: {}", path);
        } catch (Exception e) {
            throw new RuntimeException(
                    "Failed to register watch for path: " + path,
                    e
            );
        }

    }

    private static void appendEventLog(String path) {
        String entry = String.format(
                "[%s] DATA_CHANGED path=%s%n",
                Instant.now(),
                path
        );

        FileHelper.appendToFile(EVENT_LOG_FILE, entry);
    }
    private void persistLatestState(String path) {
        try {
            Stat stat = new Stat();
            byte[] data = curatorFramework.getData()
                    .storingStatIn(stat)
                    .forPath(path);

            String json = String.format(
                    "{ \"path\": \"%s\", \"data\": \"%s\", \"version\": %d, \"timestamp\": \"%s\" }",
                    path,
                    data != null ? new String(data, StandardCharsets.UTF_8) : null,
                    stat.getVersion(),
                    Instant.now()
            );

            FileHelper.writeFile(STATE_FILE, json);

        } catch (Exception e) {
            log.error("Failed to persist state for path {}", path, e);
        }
    }
}
