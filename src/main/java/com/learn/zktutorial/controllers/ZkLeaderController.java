package com.learn.zktutorial.controllers;

import com.learn.zktutorial.services.ZkLeaderElectionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/zk/leader")
@RequiredArgsConstructor
class ZkLeaderController {

    private final ZkLeaderElectionService leaderService;

    @PostMapping("/join")
    public ResponseEntity<String> joinElection() {
        leaderService.participateInElection();
        return ResponseEntity.ok("Joined leader election");
    }


    @GetMapping("/status")
    public ResponseEntity<String> status() {
        return ResponseEntity.ok(
                leaderService.isLeader()
                        ? "I AM LEADER"
                        : "I AM FOLLOWER"
        );
    }

    @GetMapping("/node")
    public ResponseEntity<String> node() {
        return ResponseEntity.ok(leaderService.getNodePath());
    }
}
