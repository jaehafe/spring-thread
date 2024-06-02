package com.spring.board.controller;

import com.spring.board.model.entity.UserEntity;
import com.spring.board.model.post.Post;
import com.spring.board.model.post.PostPatchRequestBody;
import com.spring.board.model.post.PostPostRequestBody;
import com.spring.board.model.user.LikedUser;
import com.spring.board.service.PostService;
import com.spring.board.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/posts")
public class PostController {

    private final PostService postService;
    private final UserService userService;
    private static final Logger logger = LoggerFactory.getLogger(PostController.class);

    public PostController(PostService postService, UserService userService) {
        this.postService = postService;
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<Post>> getPosts(Pageable pageable, Authentication authentication) {

        logger.info("GET /api/v1/posts");

        List<Post> posts = postService.getPosts(pageable, (UserEntity) authentication.getPrincipal());
        return ResponseEntity.ok(posts);
    }

    @GetMapping("/{postId}")
    public ResponseEntity<Post> getPostByPostId(@PathVariable("postId") Long postId, Authentication authentication) {

        logger.info("GET /api/v1/posts/{}", postId);

        Post post = postService.getPostByPostId(postId, (UserEntity) authentication.getPrincipal());
        return ResponseEntity.ok(post);
    }

    @GetMapping("/{postId}/liked-users")
    public ResponseEntity<List<LikedUser>> getLikedUsersByPostId(@PathVariable("postId") Long postId, Authentication authentication) {

        List<LikedUser> likedUsers = userService.getLikedUsersByPostId(postId, (UserEntity) authentication.getPrincipal());

        return ResponseEntity.ok(likedUsers);
    }

    @PostMapping
    public ResponseEntity<Post> createPost(@RequestBody PostPostRequestBody postPostRequestBody, Authentication authentication) {

        logger.info("POST /api/v1/posts");

        Post post = postService.createPost(postPostRequestBody, (UserEntity) authentication.getPrincipal());
        return ResponseEntity.ok(post);
    }

    @PatchMapping("/{postId}")
    public ResponseEntity<Post> updatePost(
            @PathVariable("postId") Long postId, @RequestBody PostPatchRequestBody postPatchRequestBody, Authentication authentication) {

        logger.info("PATCH /api/v1/posts/{}", postId);

        Post post = postService.updatePost(postId, postPatchRequestBody, (UserEntity) authentication.getPrincipal());
        return ResponseEntity.ok(post);
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<Void> deletePost(@PathVariable("postId") Long postId, Authentication authentication) {

        logger.info("DELETE /api/v1/posts/{}", postId);

        postService.deletePost(postId, (UserEntity) authentication.getPrincipal());
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{postId}/likes")
    public ResponseEntity<Post> toggleLike(@PathVariable("postId") Long postId, Authentication authentication) {

        Post post =  postService.toggleLike(postId, (UserEntity) authentication.getPrincipal());
        return ResponseEntity.ok(post);
    }
}
