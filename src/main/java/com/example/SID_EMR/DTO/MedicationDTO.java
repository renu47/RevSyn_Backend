package com.example.SID_EMR.DTO;

import lombok.Data;
import java.time.LocalDate;

@Data
public class MedicationDTO {
    private String name;
    private String dose;
    private LocalDate startDate;
    private LocalDate endDate;
    private String prescriber;
}
