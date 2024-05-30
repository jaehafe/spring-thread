package com.spring.board.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

public class ClientErrorException extends RuntimeException{
    private final HttpStatus status;

    public ClientErrorException(HttpStatus status, String message){
        super(message);
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }

    @RestControllerAdvice // rest api controller 전역에서 발생하는 예외를 처리하는 클래스
    public static class GlobalExceptionHandler {

        @ExceptionHandler(ClientErrorException.class)
        public ResponseEntity<ClientErrorException> handleClientErrorException(ClientErrorException e) {
            return new ResponseEntity<>(
                    new ClientErrorException(e.getStatus(), e.getMessage()),
                    e.getStatus()
            );
        }
    }
}
