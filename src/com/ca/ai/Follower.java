package com.ca.ai;

import com.ca.entities.controllers.CharacterController;

public interface Follower {

    void follow(CharacterController target);
    void stopFollowing(CharacterController target);
    
    void doMove();
}
