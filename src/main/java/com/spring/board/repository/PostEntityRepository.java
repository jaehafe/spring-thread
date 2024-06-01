package com.spring.board.repository;

import com.spring.board.model.entity.PostEntity;
import com.spring.board.model.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostEntityRepository extends JpaRepository<PostEntity, Long> {

    List<PostEntity> findByUser(UserEntity user);
}
