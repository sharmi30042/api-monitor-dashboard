package com.sharmi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sharmi.model.ApiLog;

@Repository
public interface ApiLogRepository extends JpaRepository<ApiLog, Long> {
    // Spring automatically provides methods like save(), findAll(), delete()
}