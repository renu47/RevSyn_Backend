package com.example.SID_EMR.Entity;

import jakarta.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Table(
    name = "appointments",
    uniqueConstraints = {
        @UniqueConstraint(
            name = "uk_doctor_date_time",
            columnNames = {"doctor_id", "appointment_date", "appointment_time"}
        )
    }
)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /* ===================== RELATIONSHIPS ===================== */

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "doctor_id", nullable = false)
    private Doctor doctor;

    /* ===================== PATIENT INFO ===================== */

    @Column(nullable = false)
    private String patientName;

    @Column(nullable = false)
    private String patientMobile;   // per your requirement

    /* ===================== APPOINTMENT DETAILS ===================== */

    @Column(name = "appointment_date", nullable = false)
    private LocalDate appointmentDate;

    @Column(name = "appointment_time", nullable = false)
    private LocalTime appointmentTime;

    @Column(length = 500)
    private String notes;

    /* ===================== STATUS ===================== */

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AppointmentStatus status;
    
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ailment_id", nullable = false)
    private Ailment ailment;


    /* ===================== BILLING ===================== */

    private BigDecimal consultationFees;

    private BigDecimal paidAmount;

    /* ===================== AUDIT ===================== */

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    /* ===================== CALLBACKS ===================== */

    @PrePersist
    public void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.status = AppointmentStatus.SCHEDULED;
    }

    @PreUpdate
    public void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
