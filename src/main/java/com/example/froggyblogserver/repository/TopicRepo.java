package com.example.froggyblogserver.repository;

import com.example.froggyblogserver.dto.request.TopicSearchReq;
import com.example.froggyblogserver.entity.TopicEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TopicRepo extends JpaRepository<TopicEntity, String> {
    @Query(value = "FROM TopicEntity t WHERE (:#{#req.topicName} IS NULL OR t.topicName LIKE %:#{#req.topicName}%) AND t.isDelete = 0")
    Page<TopicEntity> searchTopic(@Param("req") TopicSearchReq req, Pageable pageable);

    @Query(value = "SELECT t FROM PostTopicEntity p LEFT join TopicEntity t ON t.id = p.topicId AND t.isDelete = 0 WHERE p.postId = :postId")
    List<TopicEntity> findTopicByPostId(String postId);
    boolean existsByTopicCode(String topicCode);
}
