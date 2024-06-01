package com.spring.board.exception.follow;

import com.spring.board.exception.ClientErrorException;
import com.spring.board.model.entity.UserEntity;
import org.springframework.http.HttpStatus;

public class FollowAlreadyExistsException extends ClientErrorException {

    public FollowAlreadyExistsException() {
        super(HttpStatus.CONFLICT, "Follow already exists.");
    }

    public FollowAlreadyExistsException(UserEntity follower, UserEntity following) {
        super(HttpStatus.CONFLICT,
                "Follow already exists between " + follower.getUsername() + " and " + following.getUsername() + ".");
    }
}
