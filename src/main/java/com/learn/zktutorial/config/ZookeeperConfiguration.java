package com.learn.zktutorial.config;

import lombok.RequiredArgsConstructor;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
class ZookeeperConfiguration {

    private final ZkConfig config;

    @Bean(destroyMethod = "close")
    public CuratorFramework curatorFramework() {

        ExponentialBackoffRetry retryPolicy =
                new ExponentialBackoffRetry(1000, 3);

        CuratorFramework client =
                CuratorFrameworkFactory.builder()
                        .connectString(config.getUrl())
                        .sessionTimeoutMs(config.getSessionTimeout())
                        .connectionTimeoutMs(config.getConnectionTimeout())
                        .retryPolicy(retryPolicy)
                        .build();

        client.start();
        return client;
    }


}
