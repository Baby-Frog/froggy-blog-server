package com.example.froggyblogserver.repository;

import com.example.froggyblogserver.entity.ReportEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ReportRepo extends JpaRepository<ReportEntity,String> {
    @Query(value = "FROM ReportEntity rp WHERE rp.status = false AND rp.isDelete = false ")
    Page<ReportEntity> findAll(Pageable pageable);
}
