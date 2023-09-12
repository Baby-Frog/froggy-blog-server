package com.example.froggyblogserver.repository;

import com.example.froggyblogserver.dto.request.TopicSearchReq;
import com.example.froggyblogserver.entity.TopicEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TopicRepo extends JpaRepository<TopicEntity, String> {
    @Query(value = "FROM TopicEntity t WHERE (:#{#req.nameTopic} IS NULL OR t.topicName LIKE :#{#req.nameTopic}) ")
    Page<TopicEntity> searchTopic(@Param("req") TopicSearchReq req, Pageable pageable);
}
