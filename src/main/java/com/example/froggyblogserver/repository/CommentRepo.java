package com.example.froggyblogserver.repository;

import com.example.froggyblogserver.entity.CommentEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepo extends JpaRepository<CommentEntity, String> {
    @Query(value = "SELECT c FROM CommentEntity c WHERE c.isDelete = false AND c.postId = :postId AND c.parentId IS NULL ")
    Page<CommentEntity> findByPostId(String postId, Pageable pageable);
}
