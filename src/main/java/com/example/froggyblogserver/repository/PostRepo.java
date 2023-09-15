package com.example.froggyblogserver.repository;

import com.example.froggyblogserver.dto.request.PostSearchRequest;
import com.example.froggyblogserver.entity.PostEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepo extends JpaRepository<PostEntity, String> {

    @Query(value = "FROM PostEntity p WHERE (:#{#req.keyword} IS NULL OR p.title LIKE %:#{#req.keyword}%) " +
            "OR (:#{#req.keyword} IS NULL OR p.content LIKE %:#{#req.keyword}%) ")
    Page<PostEntity> search(@Param("req") PostSearchRequest req, Pageable pageable);
}
