package com.learn.zktutorial.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ZkNodeDataResponse {

    private String path;
    private String data;
    private int version;
}