package com.spring.board.exception.follow;

import com.spring.board.exception.ClientErrorException;
import org.springframework.http.HttpStatus;

public class InvalidFollowingException extends ClientErrorException {

    public InvalidFollowingException() {
        super(HttpStatus.BAD_REQUEST, "Invalid following request.");
    }

    public InvalidFollowingException(String message) {
        super(HttpStatus.BAD_REQUEST, message);
    }
}
