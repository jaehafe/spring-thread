package com.spring.board.model.user;

import com.spring.board.model.entity.UserEntity;

import java.time.ZonedDateTime;

public record User(
        Long userId,
        String username,
        String profile,
        String description,
        Long followerCount,
        Long followingCount,
        ZonedDateTime createdDateTime,
        ZonedDateTime updatedDateTime) {

    public static User from(UserEntity user) {
        return new User(
                user.getUserId(),
                user.getUsername(),
                user.getProfile(),
                user.getDescription(),
                user.getFollowerCount(),
                user.getFollowingCount(),
                user.getCreatedDateTime(),
                user.getUpdatedDateTime());
    }
}
