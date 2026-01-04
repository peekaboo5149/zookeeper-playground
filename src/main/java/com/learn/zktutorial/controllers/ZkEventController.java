package com.learn.zktutorial.controllers;

import com.learn.zktutorial.services.ZkEventService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/zk/events")
@RequiredArgsConstructor
class ZkEventController {

    private final ZkEventService eventService;

    /**
     * Watch a znode for data changes
     *
     * Example:
     * GET /api/zookeeper/watch/data?path=/config/app
     */
    @GetMapping("/watch/data")
    public ResponseEntity<String> watchNodeData(
            @RequestParam String path
    ) {
        eventService.watchNodeData(path);
        return ResponseEntity.ok("Watch registered for path: " + path);
    }

}
