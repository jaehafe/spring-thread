package com.spring.board.controller;

import com.spring.board.model.entity.ReplyEntity;
import com.spring.board.model.entity.UserEntity;
import com.spring.board.model.post.Post;
import com.spring.board.model.post.PostPatchRequestBody;
import com.spring.board.model.post.PostPostRequestBody;
import com.spring.board.model.reply.Reply;
import com.spring.board.model.reply.ReplyPatchRequestBody;
import com.spring.board.model.reply.ReplyPostRequestBody;
import com.spring.board.service.PostService;
import com.spring.board.service.ReplyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/posts/{postId}/replies")
public class ReplyController {

    private final ReplyService replyService;
    private static final Logger logger = LoggerFactory.getLogger(ReplyController.class);

    public ReplyController(ReplyService replyService) {
        this.replyService = replyService;
    }

    @GetMapping
    public ResponseEntity<List<Reply>> getRepliesByPostId(@PathVariable("postId") Long postId) {

        List<Reply> replies = replyService.getRepliesByPostId(postId);
        return ResponseEntity.ok(replies);
    }

    @PostMapping
    public ResponseEntity<Reply> createReply(
            @PathVariable("postId") Long postId,
            @RequestBody ReplyPostRequestBody replyPostRequestBody,
            Authentication authentication
    ) {

        Reply reply = replyService.createReply(postId, replyPostRequestBody, (UserEntity) authentication.getPrincipal());
        return ResponseEntity.ok(reply);
    }

    @PatchMapping("/{replyId}")
    public ResponseEntity<Reply> updateReply(
            @PathVariable("postId") Long postId,
            @PathVariable("replyId") Long replyId,
            @RequestBody ReplyPatchRequestBody replyPatchRequestBody,
            Authentication authentication
    ) {

        Reply reply = replyService.updateReply(postId, replyId, replyPatchRequestBody, (UserEntity) authentication.getPrincipal());
        return ResponseEntity.ok(reply);
    }

    @DeleteMapping("/{replyId}")
    public ResponseEntity<Void> deletePost(
            @PathVariable("postId") Long postId,
            @PathVariable("replyId") Long replyId,
            Authentication authentication
    ) {

        replyService.deleteReply(postId, replyId, (UserEntity) authentication.getPrincipal());

        return ResponseEntity.noContent().build();
    }
}
