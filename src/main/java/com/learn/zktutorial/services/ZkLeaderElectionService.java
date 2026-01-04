package com.learn.zktutorial.services;

public interface ZkLeaderElectionService {
    void participateInElection();

    boolean isLeader();

    String getNodePath();

}
