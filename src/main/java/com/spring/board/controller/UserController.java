package com.spring.board.controller;

import com.spring.board.model.entity.UserEntity;
import com.spring.board.model.post.Post;
import com.spring.board.model.user.*;
import com.spring.board.service.PostService;
import com.spring.board.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;
    private final PostService postService;

    public UserController(UserService userService, PostService postService) {
        this.userService = userService;
        this.postService = postService;
    }

    @GetMapping
    public ResponseEntity<List<User>> getUsers(@RequestParam(name = "query", required = false) String query) {

        List<User> users = userService.getUsers(query);
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{username}")
    public ResponseEntity<User> getUser(@PathVariable("username") String username) {

        User user = userService.getUser(username);
        return ResponseEntity.ok(user);
    }

    @PatchMapping("/{username}")
    public ResponseEntity<User> updateUser(
            @PathVariable("username") String username,
            @RequestBody UserPatchRequestBody requestBody,
            Authentication authentication
            ) {

        User user = userService.updateUser(username, requestBody, (UserEntity) authentication.getPrincipal());
        return ResponseEntity.ok(user);
    }

    @GetMapping("/{username}/posts")
    public ResponseEntity<List<Post>> getPostsByUsername(@PathVariable("username") String username) {

        var posts = postService.getPostsByUsername(username);

        return ResponseEntity.ok(posts);
    }

    @PostMapping
    public ResponseEntity<User> signUp(@Valid @RequestBody UserSignUpRequestBody requestBody) {

        User user = userService.signUp(requestBody.username(), requestBody.password());
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PostMapping("/authenticate")
    public ResponseEntity<UserAuthenticationResponse> authenticate(@Valid @RequestBody UserLoginRequestBody userLoginRequestBody) {

        var response = userService.authenticate(userLoginRequestBody.username(), userLoginRequestBody.password());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{username}/follows")
    public ResponseEntity<User> follow(@PathVariable("username") String username, Authentication authentication) {

        User user = userService.follow(username, (UserEntity) authentication.getPrincipal());
        return ResponseEntity.ok(user);
    }

    @DeleteMapping("/{username}/follows")
    public ResponseEntity<User> unfollow(@PathVariable("username") String username, Authentication authentication) {

        User user = userService.unFollow(username, (UserEntity) authentication.getPrincipal());
        return ResponseEntity.ok(user);
    }

    // 팔로워 목록 조회
    @GetMapping("/{username}/followers")
    public ResponseEntity<List<User>> getFollowersByUser(@PathVariable("username") String username) {

        List<User> followers = userService.getFollowersByUser(username);
        return ResponseEntity.ok(followers);
    }

    // 팔로잉 목록 조회
    @GetMapping("/{username}/followings")
    public ResponseEntity<List<User>> getFollowingsByUser(@PathVariable("username") String username) {

        List<User> followings = userService.getFollowingsByUser(username);
        return ResponseEntity.ok(followings);
    }
}
