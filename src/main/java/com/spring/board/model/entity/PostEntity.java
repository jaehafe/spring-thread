package com.spring.board.model.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.time.ZonedDateTime;
import java.util.Objects;

@Entity
@Table(name = "post")
@SQLDelete(sql = "UPDATE \"post\" SET deleteddatetime = CURRENT_TIMESTAMP WHERE postid = ?")
@SQLRestriction("deleteddatetime IS NULL")
public class PostEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long postId;

    @Column(columnDefinition = "TEXT")
    private String body;

    @Column
    private ZonedDateTime createdDateTIme;

    @Column
    private ZonedDateTime updatedDateTIme;

    @Column
    private ZonedDateTime deletedDateTIme;

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

    public ZonedDateTime getCreatedDateTIme() {
        return createdDateTIme;
    }

    public void setCreatedDateTIme(ZonedDateTime createdDateTIme) {
        this.createdDateTIme = createdDateTIme;
    }

    public ZonedDateTime getUpdatedDateTIme() {
        return updatedDateTIme;
    }

    public void setUpdatedDateTIme(ZonedDateTime updatedDateTIme) {
        this.updatedDateTIme = updatedDateTIme;
    }

    public ZonedDateTime getDeletedDateTIme() {
        return deletedDateTIme;
    }

    public void setDeletedDateTIme(ZonedDateTime deletedDateTIme) {
        this.deletedDateTIme = deletedDateTIme;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PostEntity that = (PostEntity) o;
        return Objects.equals(postId, that.postId) && Objects.equals(body, that.body) && Objects.equals(createdDateTIme, that.createdDateTIme) && Objects.equals(updatedDateTIme, that.updatedDateTIme) && Objects.equals(deletedDateTIme, that.deletedDateTIme);
    }

    @Override
    public int hashCode() {
        return Objects.hash(postId, body, createdDateTIme, updatedDateTIme, deletedDateTIme);
    }
}
