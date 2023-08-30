package com.example.froggyblogserver.repository;

import com.example.froggyblogserver.dto.UserSearchRequest;
import com.example.froggyblogserver.entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepo extends JpaRepository<UserEntity, String> {
    @Query(value = "from UserEntity u WHERE (:#{#req.name} IS NULL OR u.name like %:#{#req.name}%) ORDER BY u.createDate")
    Page<UserEntity> search(@Param("req") UserSearchRequest request, Pageable pageable);
}
