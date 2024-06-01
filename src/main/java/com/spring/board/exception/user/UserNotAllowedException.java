package com.spring.board.exception.user;

import com.spring.board.exception.ClientErrorException;
import org.springframework.http.HttpStatus;

public class UserNotAllowedException extends ClientErrorException {

    public UserNotAllowedException() {
        super(HttpStatus.FORBIDDEN, "User not allowed.");
    }
}
