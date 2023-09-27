package com.example.froggyblogserver.repository;

import com.example.froggyblogserver.entity.LikesEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LikeRepo extends JpaRepository<LikesEntity,String> {
    LikesEntity findByUserIdAndPostId(String userId,String postId);
    @Query(value = "SELECT count(l) FROM LikesEntity l WHERE l.postId = :postId AND l.isDelete = false ")
    Optional<Long> countByPostId(@Param("postId") String postId);
}
