package com.example.SID_EMR.Util;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.SID_EMR.Entity.AuditLog;
import com.example.SID_EMR.Repository.AuditLogRepository;


@Component
public class Logging {
	@Autowired
	AuditLogRepository auditLogRepository;
	
	 public void logAction(String entity, Long entityId, String action, String performedBy, String details) {
	        AuditLog log = AuditLog.builder()
	                .entity(entity)
	                .entityId(entityId)
	                .action(action)
	                .performedBy(performedBy)
	                .timestamp(LocalDateTime.now())
	                .details(details)
	                .build();
	        auditLogRepository.save(log);
	    }

}
