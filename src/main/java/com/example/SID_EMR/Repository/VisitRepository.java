package com.example.SID_EMR.Repository;

import com.example.SID_EMR.Entity.Visit;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VisitRepository extends JpaRepository<Visit, Long> {
}