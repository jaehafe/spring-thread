package com.spring.board.model.post;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.spring.board.model.entity.PostEntity;
import com.spring.board.model.user.User;

import java.time.ZonedDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record Post(
        Long postId,
        String body,
        User user,
        ZonedDateTime createdDateTime,
        ZonedDateTime updatedDateTime,
        ZonedDateTime deletedDateTime) {
    public static Post from(PostEntity postEntity) {
        return new Post(
                postEntity.getPostId(),
                postEntity.getBody(),
                User.from(postEntity.getUser()),
                postEntity.getCreatedDateTime(),
                postEntity.getUpdatedDateTime(),
                postEntity.getDeletedDateTime());
    }
}