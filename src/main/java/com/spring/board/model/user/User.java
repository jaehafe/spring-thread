package com.spring.board.model.user;

import com.spring.board.model.entity.UserEntity;

import java.time.ZonedDateTime;

public record User(
        Long userId,
        String username,
        String profile,
        String description,
        Long followersCount,
        Long followingsCount,
        ZonedDateTime createdDateTime,
        ZonedDateTime updatedDateTime,
        Boolean isFollowing
) {

    public static User from(UserEntity user) {
        return new User(
                user.getUserId(),
                user.getUsername(),
                user.getProfile(),
                user.getDescription(),
                user.getFollowersCount(),
                user.getFollowingsCount(),
                user.getCreatedDateTime(),
                user.getUpdatedDateTime(),
                null
        );
    }

    public static User from(UserEntity user, boolean isFollowing) {
        return new User(
                user.getUserId(),
                user.getUsername(),
                user.getProfile(),
                user.getDescription(),
                user.getFollowersCount(),
                user.getFollowingsCount(),
                user.getCreatedDateTime(),
                user.getUpdatedDateTime(),
                isFollowing
        );
    }
}
