package com.spring.board.model.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.time.ZonedDateTime;
import java.util.Objects;

@Entity
@Table(name = "post", indexes = {@Index(name = "post_userid_idx", columnList = "userid")})
@SQLDelete(sql = "UPDATE \"post\" SET deleteddatetime = CURRENT_TIMESTAMP WHERE postid = ?")
// Deprecated in Hibernate 6.3
// @Where(clause = "deletedDateTime IS NULL")
@SQLRestriction("deleteddatetime IS NULL")
public class PostEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long postId;

    @Column(columnDefinition = "TEXT")
    private String body;

    @Column private ZonedDateTime createdDateTime;

    @Column private ZonedDateTime updatedDateTime;

    @Column private ZonedDateTime deletedDateTime;

    @ManyToOne
    @JoinColumn(name = "userid")
    private UserEntity user;

    public PostEntity() {}

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

    public ZonedDateTime getDeletedDateTime() {
        return deletedDateTime;
    }

    public void setDeletedDateTime(ZonedDateTime deletedDateTime) {
        this.deletedDateTime = deletedDateTime;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PostEntity that = (PostEntity) o;
        return Objects.equals(postId, that.postId) && Objects.equals(body, that.body) && Objects.equals(createdDateTime, that.createdDateTime) && Objects.equals(updatedDateTime, that.updatedDateTime) && Objects.equals(deletedDateTime, that.deletedDateTime) && Objects.equals(user, that.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(postId, body, createdDateTime, updatedDateTime, deletedDateTime, user);
    }

    @PrePersist
    private void prePersist() {
        this.createdDateTime = ZonedDateTime.now();
        this.updatedDateTime = this.createdDateTime;
    }

    @PreUpdate
    private void preUpdate() {
        this.updatedDateTime = ZonedDateTime.now();
    }
}
