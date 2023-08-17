package com.example.froggyblogserver.repository;

import com.example.froggyblogserver.entity.TopicEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TopicRepo extends JpaRepository<TopicEntity, String> {
}
