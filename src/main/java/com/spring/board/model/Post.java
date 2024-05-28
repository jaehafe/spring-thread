package com.spring.board.model;

import java.time.ZonedDateTime;
import java.util.Objects;

public class Post {
    private Long postId;
    private String body;
    private ZonedDateTime createdDateTime;
    private ZonedDateTime updatedDateTime;

    public Post(Long postId, String body, ZonedDateTime createdDateTime, ZonedDateTime updatedDateTime) {
        this.postId = postId;
        this.body = body;
        this.createdDateTime = createdDateTime;
        this.updatedDateTime = updatedDateTime;
    }

    public Long getPostId() {
        return postId;
    }

    public void setPostId(Long postId) {
        this.postId = postId;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public ZonedDateTime getCreatedDateTime() {
        return createdDateTime;
    }

    public void setCreatedDateTime(ZonedDateTime createdDateTime) {
        this.createdDateTime = createdDateTime;
    }

    public ZonedDateTime getUpdatedDateTime() {
        return updatedDateTime;
    }

    public void setUpdatedDateTime(ZonedDateTime updatedDateTime) {
        this.updatedDateTime = updatedDateTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Post post = (Post) o;
        return Objects.equals(postId, post.postId) && Objects.equals(body, post.body) && Objects.equals(createdDateTime, post.createdDateTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(postId, body, createdDateTime);
    }

    @Override
    public String toString() {
        return "Post{" +
                "postId=" + postId +
                ", body='" + body + '\'' +
                ", createdDateTime=" + createdDateTime +
                '}';
    }
}
