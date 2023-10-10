package com.example.froggyblogserver.repository;

import com.example.froggyblogserver.entity.UserPostEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserPostRepo extends JpaRepository<UserPostEntity,String> {
    Optional<UserPostEntity> findByUserIdAndPostId(String userId,String postId);
}
