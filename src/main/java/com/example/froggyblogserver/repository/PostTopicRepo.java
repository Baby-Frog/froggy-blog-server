package com.example.froggyblogserver.repository;

import com.example.froggyblogserver.entity.PostTopicEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostTopicRepo extends JpaRepository<PostTopicEntity,String> {
}
