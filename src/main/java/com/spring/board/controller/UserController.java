package com.spring.board.controller;

import com.spring.board.model.entity.UserEntity;
import com.spring.board.model.post.Post;
import com.spring.board.model.reply.Reply;
import com.spring.board.model.user.*;
import com.spring.board.service.PostService;
import com.spring.board.service.ReplyService;
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
    private final ReplyService replyService;

    public UserController(UserService userService, PostService postService, ReplyService replyService) {
        this.userService = userService;
        this.postService = postService;
        this.replyService = replyService;
    }

    @GetMapping
    public ResponseEntity<List<User>> getUsers(@RequestParam(name = "query", required = false) String query, Authentication authentication) {

        List<User> users = userService.getUsers(query, (UserEntity) authentication.getPrincipal());
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{username}")
    public ResponseEntity<User> getUser(@PathVariable("username") String username, Authentication authentication) {

        User user = userService.getUser(username, (UserEntity) authentication.getPrincipal());
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
    public ResponseEntity<List<Post>> getPostsByUsername(@PathVariable("username") String username, Authentication authentication) {

        var posts = postService.getPostsByUsername(username, (UserEntity) authentication.getPrincipal());

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

    @GetMapping("/{username}/followers")
    public ResponseEntity<List<Follower>> getFollowersByUser(@PathVariable("username") String username, Authentication authentication) {
        var followers = userService.getFollowersByUsername(username, (UserEntity) authentication.getPrincipal());
        return new ResponseEntity<>(followers, HttpStatus.OK);
    }

    @GetMapping("/{username}/followings")
    public ResponseEntity<List<User>> getFollowingsByUser(@PathVariable("username") String username, Authentication authentication) {
        var followings = userService.getFollowingsByUsername(username, (UserEntity) authentication.getPrincipal());
        return new ResponseEntity<>(followings, HttpStatus.OK);
    }

    @GetMapping("/{username}/liked-users")
    public ResponseEntity<List<LikedUser>> getLikedUsersByUser(@PathVariable("username") String username, Authentication authentication) {
        var likedUsers = userService.getLikedUsersByUser(username, (UserEntity) authentication.getPrincipal());
        return ResponseEntity.ok(likedUsers);
    }

    @GetMapping("/{username}/replies")
    public ResponseEntity<List<Reply>> getRepliesByUser(@PathVariable("username") String username) {
        List<Reply> replies = replyService.getRepliesByUser(username);
        return ResponseEntity.ok(replies);
    }
}
