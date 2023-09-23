package com.example.froggyblogserver.repository;

import com.example.froggyblogserver.entity.ImageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImageRepo extends JpaRepository<ImageEntity,String> {
}
