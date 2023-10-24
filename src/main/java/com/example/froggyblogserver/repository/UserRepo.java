package com.example.froggyblogserver.repository;

import com.example.froggyblogserver.dto.request.UserSearchRequest;
import com.example.froggyblogserver.entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepo extends JpaRepository<UserEntity, String> {
    @Query(value = "SELECT u from UserEntity u WHERE (:#{#req.name} IS NULL OR u.fullName like %:#{#req.name}%) ")
    Page<UserEntity> search(@Param("req") UserSearchRequest request, Pageable pageable);

    @Query(value = "from UserEntity u WHERE (:email IS NULL OR u.email = :email) AND (:provider IS NULL OR u.provider = :provider)")
    Optional<UserEntity> findByEmailanAndProvider(@Param("email") String email,@Param("provider") String provider);

}
