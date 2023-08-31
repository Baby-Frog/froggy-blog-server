package com.example.froggyblogserver.repository;

import com.example.froggyblogserver.entity.UserPostEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserPostRepo extends JpaRepository<UserPostEntity,String> {
}
