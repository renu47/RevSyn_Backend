package com.example.SID_EMR.DTO;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class AppointmentRequestDTO {

    private Long doctorId;
    private String patientName;
    private String patientMobile;
    private Long ailmentId;           // for mapping
    private LocalDate appointmentDate;
    private LocalTime appointmentTime;
    private String notes;             // newly added
    private BigDecimal paidAmount;    // optional, can be null
}
