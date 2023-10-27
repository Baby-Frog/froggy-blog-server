package com.example.froggyblogserver.repository;

import com.example.froggyblogserver.entity.PostEntity;
import com.example.froggyblogserver.entity.PostTopicEntity;
import com.example.froggyblogserver.entity.TopicEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostTopicRepo extends JpaRepository<PostTopicEntity,String> {
    @Query(value = "FROM PostTopicEntity pt WHERE pt.postId =:postId AND pt.isDelete = 0")
    List<PostTopicEntity> findByPostId(String postId);
}
