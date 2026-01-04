package com.learn.zktutorial.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ZkUpdateRequest {

    private String data;
    private int expectedVersion;
}
