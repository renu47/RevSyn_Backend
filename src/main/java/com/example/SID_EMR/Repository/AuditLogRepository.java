package com.example.SID_EMR.Repository;

import com.example.SID_EMR.Entity.AuditLog;


import org.springframework.data.jpa.repository.JpaRepository;

public interface AuditLogRepository extends JpaRepository<AuditLog, Long> {
}
