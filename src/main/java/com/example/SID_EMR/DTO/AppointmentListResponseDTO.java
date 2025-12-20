package com.example.SID_EMR.DTO;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

import com.example.SID_EMR.Entity.AppointmentStatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AppointmentListResponseDTO {

    private Long id;

    private String patientName;
    private String patientMobile;

    private Long doctorId;
    private String doctorName;

    private Long ailmentId;
    private String ailmentName;

    private LocalDate appointmentDate;
    private LocalTime appointmentTime;

    private BigDecimal consultationFees;

    private AppointmentStatus status;
}
