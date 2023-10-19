package com.example.froggyblogserver.repository;

import com.example.froggyblogserver.dto.CommentDto;
import com.example.froggyblogserver.entity.CommentEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CommentRepo extends JpaRepository<CommentEntity, String> {
    @Query(value = "SELECT c FROM CommentEntity c WHERE c.isDelete = 0 and c.parentId is null and c.postId = :postId")
    Page<CommentEntity> findByPostId(String postId, Pageable pageable);
    @Query(value = "SELECT c FROM CommentEntity c WHERE c.isDelete = 0 and c.parentId = :parentId")
    Page<CommentEntity> findByParentId(String parentId,Pageable pageable);
    @Query(value = "SELECT COUNT(c) FROM CommentEntity c WHERE c.parentId = :id AND c.isDelete = 0")
    Optional<Integer> countByParentId(String id);
    @Query(value = "SELECT COUNT(c) FROM CommentEntity c WHERE c.postId = :postId AND c.isDelete = 0")
    Optional<Integer> countByPostId(String postId);
}
