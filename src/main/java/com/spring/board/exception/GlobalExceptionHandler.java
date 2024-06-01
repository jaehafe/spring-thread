package com.spring.board.exception;

import com.spring.board.model.error.ClientErrorResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice // rest api controller 전역에서 발생하는 예외를 처리하는 클래스
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(ClientErrorException.class)
    public ResponseEntity<ClientErrorResponse> handleClientErrorException(ClientErrorException e) {

        logger.error("ClientErrorException: ", e);

        return new ResponseEntity<>(
                new ClientErrorResponse(e.getStatus(), e.getMessage()),
                e.getStatus()
        );
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ClientErrorResponse> handleClientErrorException(
            HttpMessageNotReadableException e) {

        logger.error("ClientErrorException: ", e);

        return new ResponseEntity<>(
                new ClientErrorResponse(HttpStatus.BAD_REQUEST, e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ClientErrorResponse> handleClientErrorException(
            MethodArgumentNotValidException e) {

        logger.error("MethodArgumentNotValidException: ", e);

        var errorMessage =
                e.getFieldErrors().stream()
                        .map(fieldError -> (fieldError.getField() + ": " + fieldError.getDefaultMessage()))
                        .toList()
                        .toString();

        return new ResponseEntity<>(
                new ClientErrorResponse(HttpStatus.BAD_REQUEST, errorMessage), HttpStatus.BAD_REQUEST);
    }

    // 서버 내부, 나머지 에러
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ClientErrorResponse> handleRuntimeException(RuntimeException e) {

        logger.error("RuntimeException: ", e);

        return ResponseEntity.internalServerError().build();
    }

    // 서버 내부, 나머지 에러
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ClientErrorResponse> handleRuntimeException(Exception e) {

        logger.error("Exception: ", e);

        return ResponseEntity.internalServerError().build();
    }
}
