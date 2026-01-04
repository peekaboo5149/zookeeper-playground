package com.learn.zktutorial.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Component
@ConfigurationProperties("client.zookeeper")
class ZkConfig {
    private String url;
    private int sessionTimeout;
    private int connectionTimeout;
}
