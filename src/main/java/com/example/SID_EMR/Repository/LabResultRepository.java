package com.example.SID_EMR.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.SID_EMR.Entity.LabResult;

public interface LabResultRepository extends JpaRepository<LabResult, Long> {
}