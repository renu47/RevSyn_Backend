package com.example.SID_EMR.DTO;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
@Builder
public class AppointmentResponseDTO {

    private Long id;
    private Long doctorId;
    private String doctorName;
    private String patientName;
    private String patientMobile;
    private Long ailmentId;
    private String ailmentName;
    private LocalDate appointmentDate;
    private LocalTime appointmentTime;
    private String notes;
    private String status;
    private BigDecimal consultationFees;
    private BigDecimal paidAmount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
