package com.spring.board.service;

import com.spring.board.exception.post.PostNotFoundException;
import com.spring.board.exception.user.UserNotAllowedException;
import com.spring.board.exception.user.UserNotFoundException;
import com.spring.board.model.entity.LikeEntity;
import com.spring.board.model.entity.PostEntity;
import com.spring.board.model.entity.UserEntity;
import com.spring.board.model.post.Post;
import com.spring.board.model.post.PostPatchRequestBody;
import com.spring.board.model.post.PostPostRequestBody;
import com.spring.board.repository.LikeEntityRepository;
import com.spring.board.repository.PostEntityRepository;
import com.spring.board.repository.UserEntityRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PostService {

    private final PostEntityRepository postEntityRepository;
    private final UserEntityRepository userEntityRepository;
    private final LikeEntityRepository likeEntityRepository;

    public PostService(PostEntityRepository postEntityRepository, UserEntityRepository userEntityRepository, LikeEntityRepository likeEntityRepository) {
        this.postEntityRepository = postEntityRepository;
        this.userEntityRepository = userEntityRepository;
        this.likeEntityRepository = likeEntityRepository;
    }

    public List<Post> getPosts(Pageable pageable, UserEntity currentUser) {
        Page<PostEntity> postEntities = postEntityRepository.findAll(pageable);
        return postEntities.stream()
                .map(postEntity -> getPostWithLikingStatus(postEntity, currentUser))
                .toList();
    }

    public Post getPostByPostId(Long postId, UserEntity currentUser) {

        PostEntity postEntity =
                postEntityRepository.findById(postId).orElseThrow(() -> new PostNotFoundException(postId));

        return getPostWithLikingStatus(postEntity, currentUser);
    }

    public Post getPostWithLikingStatus(PostEntity postEntity, UserEntity currentUser) {

        Boolean isLiking = likeEntityRepository.findByUserAndPost(currentUser, postEntity).isPresent();
        return Post.from(postEntity, isLiking);
    }

    public Post createPost(PostPostRequestBody postPostRequestBody, UserEntity currentUser) {

        PostEntity postEntity = postEntityRepository.save(PostEntity.of(postPostRequestBody.body(), currentUser));
//        var postEntity = new PostEntity();
//        postEntity.setBody(postPostRequestBody.body());
//        postEntity.setUser(currentUser);
//        var savedPostEntity = postEntityRepository.save(postEntity);
        return Post.from(postEntity);
    }

    public Post updatePost(Long postId, PostPatchRequestBody postPatchRequestBody, UserEntity currentUser) {

        PostEntity postEntity =
                postEntityRepository.findById(postId).orElseThrow(() -> new PostNotFoundException(postId));

        if(!postEntity.getUser().equals(currentUser)) {
            throw new UserNotAllowedException();
        }

        postEntity.setBody(postPatchRequestBody.body());
        var updatedEntity = postEntityRepository.save(postEntity);
        return Post.from(updatedEntity);
    }

    public void deletePost(Long postId, UserEntity currentUser) {

        PostEntity postEntity =
                postEntityRepository.findById(postId).orElseThrow(() -> new PostNotFoundException(postId));

        if(!postEntity.getUser().equals(currentUser)) {
            throw new UserNotAllowedException();
        }

        postEntityRepository.delete(postEntity);
    }


    public List<Post> getPostsByUsername(String username, UserEntity currentUser) {

        UserEntity userEntity = userEntityRepository
                .findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(username));

        List<PostEntity> postEntities = postEntityRepository.findByUser(userEntity);
        return postEntities.stream().map(postEntity -> getPostWithLikingStatus(postEntity, currentUser)).toList();
    }

    @Transactional
    public Post toggleLike(Long postId, UserEntity currentUser) {

        PostEntity postEntity =
                postEntityRepository.findById(postId).orElseThrow(() -> new PostNotFoundException(postId));

        var likeEntity =
                likeEntityRepository.findByUserAndPost(currentUser, postEntity);

        if (likeEntity.isPresent()) {
            likeEntityRepository.delete(likeEntity.get());
            postEntity.setLikesCount(Math.max(0, postEntity.getLikesCount() - 1));
            return Post.from(postEntityRepository.save(postEntity), false);
        } else {
            likeEntityRepository.save(LikeEntity.of(currentUser, postEntity));
            postEntity.setLikesCount(postEntity.getLikesCount() + 1);
            return Post.from(postEntityRepository.save(postEntity), true);
        }
    }
}
