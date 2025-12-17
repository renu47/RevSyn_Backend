package com.example.SID_EMR.DTO;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.Set;

@Data
@Builder
public class DoctorResponseDTO {

    private Long id;
    private String name;
    private String email;
    private String mobile;
    private BigDecimal consultationFees;
    private boolean active;
    private LocalTime startTime;
    private LocalTime endTime;
    private Integer slotDuration;
    private Set<DayOfWeek> availableDays;
    private Set<String> ailments;
}
