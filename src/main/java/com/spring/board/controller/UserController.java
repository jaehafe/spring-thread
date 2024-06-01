package com.spring.board.controller;

import com.spring.board.model.user.User;
import com.spring.board.model.user.UserSignUpRequestBody;
import com.spring.board.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<User> signUp(@RequestBody UserSignUpRequestBody requestBody) {
        var user = userService.signUp(requestBody.username(), requestBody.password());
        return new ResponseEntity<>(user, HttpStatus.OK);
    }
}
