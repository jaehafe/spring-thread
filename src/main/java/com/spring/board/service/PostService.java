package com.spring.board.service;

import com.spring.board.exception.post.PostNotFoundException;
import com.spring.board.exception.user.UserNotAllowedException;
import com.spring.board.exception.user.UserNotFoundException;
import com.spring.board.model.entity.PostEntity;
import com.spring.board.model.entity.UserEntity;
import com.spring.board.model.post.Post;
import com.spring.board.model.post.PostPatchRequestBody;
import com.spring.board.model.post.PostPostRequestBody;
import com.spring.board.repository.PostEntityRepository;
import com.spring.board.repository.UserEntityRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PostService {

    private final PostEntityRepository postEntityRepository;
    private final UserEntityRepository userEntityRepository;

    public PostService(PostEntityRepository postEntityRepository, UserEntityRepository userEntityRepository) {
        this.postEntityRepository = postEntityRepository;
        this.userEntityRepository = userEntityRepository;
    }

    public List<Post> getPosts() {
        List<PostEntity> postEntities = postEntityRepository.findAll();
        return postEntities.stream().map(Post::from).toList();
    }

    public Post getPostByPostId(Long postId) {

        PostEntity postEntity =
                postEntityRepository.findById(postId).orElseThrow(() -> new PostNotFoundException(postId));
        return Post.from(postEntity);
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


    public List<Post> getPostsByUsername(String username) {

        UserEntity userEntity = userEntityRepository
                .findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(username));

        List<PostEntity> postEntities = postEntityRepository.findByUser(userEntity);
        return postEntities.stream().map(Post::from).toList();
    }
}
