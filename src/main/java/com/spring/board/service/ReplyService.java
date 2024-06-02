package com.spring.board.service;

import com.spring.board.exception.post.PostNotFoundException;
import com.spring.board.exception.reply.ReplyNotFoundException;
import com.spring.board.exception.user.UserNotAllowedException;
import com.spring.board.exception.user.UserNotFoundException;
import com.spring.board.model.entity.PostEntity;
import com.spring.board.model.entity.ReplyEntity;
import com.spring.board.model.entity.UserEntity;
import com.spring.board.model.post.Post;
import com.spring.board.model.post.PostPatchRequestBody;
import com.spring.board.model.post.PostPostRequestBody;
import com.spring.board.model.reply.Reply;
import com.spring.board.model.reply.ReplyPatchRequestBody;
import com.spring.board.model.reply.ReplyPostRequestBody;
import com.spring.board.repository.PostEntityRepository;
import com.spring.board.repository.ReplyEntityRepository;
import com.spring.board.repository.UserEntityRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReplyService {

    private final ReplyEntityRepository replyEntityRepository;
    private final PostEntityRepository postEntityRepository;
    private final UserEntityRepository userEntityRepository;

    public ReplyService(ReplyEntityRepository replyEntityRepository, PostEntityRepository postEntityRepository, UserEntityRepository userEntityRepository) {
        this.replyEntityRepository = replyEntityRepository;
        this.postEntityRepository = postEntityRepository;
        this.userEntityRepository = userEntityRepository;
    }

    public List<Reply> getRepliesByPostId(Long postId) {

        PostEntity postEntity =
                postEntityRepository.findById(postId).orElseThrow(() -> new PostNotFoundException(postId));

        List<ReplyEntity> replyEntity =
                replyEntityRepository.findByPost(postEntity);

        return replyEntity.stream().map(Reply::from).toList();
    }

    @Transactional
    public Reply createReply(Long postId, ReplyPostRequestBody replyPostRequestBody, UserEntity currentUser) {

        PostEntity postEntity =
                postEntityRepository.findById(postId).orElseThrow(() -> new PostNotFoundException(postId));

        ReplyEntity replyEntity =
                replyEntityRepository.save(ReplyEntity.of(replyPostRequestBody.body(), currentUser, postEntity));

        postEntity.setRepliesCount(postEntity.getRepliesCount() + 1);

        return Reply.from(replyEntity);
    }

    public Reply updateReply(Long postId, Long replyId, ReplyPatchRequestBody replyPatchRequestBody, UserEntity currentUser) {

        ReplyEntity replyEntity =
                replyEntityRepository.findById(replyId).orElseThrow(() -> new ReplyNotFoundException(replyId));

        if(!replyEntity.getUser().equals(currentUser)) {
            throw new UserNotAllowedException();
        }

        replyEntity.setBody(replyPatchRequestBody.body());
        return Reply.from(replyEntityRepository.save(replyEntity));
    }

    @Transactional
    public void deleteReply(Long postId, Long replyId, UserEntity currentUser) {

        PostEntity postEntity =
                postEntityRepository.findById(postId).orElseThrow(() -> new PostNotFoundException(postId));

        ReplyEntity replyEntity =
                replyEntityRepository.findById(replyId).orElseThrow(() -> new ReplyNotFoundException(replyId));

        if(!replyEntity.getUser().equals(currentUser)) {
            throw new UserNotAllowedException();
        }

        replyEntityRepository.delete(replyEntity);

        postEntity.setRepliesCount(Math.max(0, postEntity.getRepliesCount() - 1));
        postEntityRepository.save(postEntity);
    }

    public List<Reply> getRepliesByUser(String username) {

        UserEntity userEntity = userEntityRepository.findByUsername(username).orElseThrow(() -> new UserNotFoundException(username));

        List<ReplyEntity> replyEntities = replyEntityRepository.findByUser(userEntity);

        return replyEntities.stream().map(Reply::from).toList();
    }
}
