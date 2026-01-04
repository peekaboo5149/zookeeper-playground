package com.learn.zktutorial.controllers;

import com.learn.zktutorial.dto.request.ZkUpdateRequest;
import com.learn.zktutorial.dto.response.ZkChildrenResponse;
import com.learn.zktutorial.dto.response.ZkNodeDataResponse;
import com.learn.zktutorial.dto.response.ZkOperationResponse;
import com.learn.zktutorial.dto.response.ZkSequentialResponse;
import com.learn.zktutorial.services.ZkService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/zk/operations")
@RequiredArgsConstructor
class ZkController {

    private final ZkService zkService;

    @PostMapping("/persistent")
    public ResponseEntity<ZkOperationResponse> createPersistentNode(
            @RequestParam String path,
            @RequestParam String data
    ) {
        return ResponseEntity.ok(
                zkService.createPersistentNode(path, data)
        );
    }

    @PostMapping("/ephemeral")
    public ResponseEntity<ZkOperationResponse> createEphemeralNode(
            @RequestParam String path,
            @RequestParam String data
    ) {
        return ResponseEntity.ok(
                zkService.createEphemeralNode(path, data)
        );
    }

    @GetMapping("/data")
    public ResponseEntity<ZkNodeDataResponse> getNodeData(
            @RequestParam String path
    ) {
        return ResponseEntity.ok(
                zkService.getNodeData(path)
        );
    }

    @GetMapping("/children")
    public ResponseEntity<ZkChildrenResponse> getChildren(
            @RequestParam String path
    ) {
        return ResponseEntity.ok(
                zkService.getChildren(path)
        );
    }

    @PostMapping("/sequential")
    public ResponseEntity<ZkSequentialResponse> createSequentialNode(
            @RequestParam String path
    ) {
        return ResponseEntity.ok(
                zkService.createSequentialNode(path)
        );
    }
    /**
     * 7️⃣ Update znode data using optimistic locking
     *
     * Example:
     * PUT /api/zookeeper
     * Body:
     * {
     *   "data": "new-value",
     *   "expectedVersion": 1
     * }
     */
    @PutMapping
    public ResponseEntity<ZkOperationResponse> updateNode(
            @RequestParam String path,
            @RequestBody ZkUpdateRequest request
    ) {
        return ResponseEntity.ok(
                zkService.updateNode(
                        path,
                        request
                )
        );
    }


    @DeleteMapping
    public ResponseEntity<ZkOperationResponse> deleteNode(
            @RequestParam String path
    ) {
        return ResponseEntity.ok(
                zkService.deleteNode(path)
        );
    }
}
