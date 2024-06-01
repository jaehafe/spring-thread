package com.spring.board.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
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
@Getter
@Setter
public class PostEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long postId;

    @Column(columnDefinition = "TEXT")
    private String body;

    @Column
    private Long repliesCount = 0L;

    @Column
    private Long likesCount = 0L;

    @Column private ZonedDateTime createdDateTime;

    @Column private ZonedDateTime updatedDateTime;

    @Column private ZonedDateTime deletedDateTime;

    @ManyToOne
    @JoinColumn(name = "userid")
    private UserEntity user;

    public PostEntity() {}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PostEntity post = (PostEntity) o;
        return Objects.equals(postId, post.postId) && Objects.equals(body, post.body) && Objects.equals(repliesCount, post.repliesCount) && Objects.equals(likesCount, post.likesCount) && Objects.equals(createdDateTime, post.createdDateTime) && Objects.equals(updatedDateTime, post.updatedDateTime) && Objects.equals(deletedDateTime, post.deletedDateTime) && Objects.equals(user, post.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(postId, body, repliesCount, likesCount, createdDateTime, updatedDateTime, deletedDateTime, user);
    }

    public static PostEntity of(String body, UserEntity user) {
        PostEntity post = new PostEntity();
        post.setBody(body);
        post.setUser(user);
        return post;
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
