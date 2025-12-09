package com.example.SID_EMR.Entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuditLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String entity;    // e.g., "Patient", "Visit"
    private Long entityId;    // ID of affected entity
    private String action;    // CREATE, UPDATE, DELETE
    private String performedBy;
    private LocalDateTime timestamp;

    @Column(length = 2000)
    private String details;
}