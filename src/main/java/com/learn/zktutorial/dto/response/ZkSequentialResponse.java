package com.learn.zktutorial.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ZkSequentialResponse {

    private String basePath;
    private String createdPath;
}