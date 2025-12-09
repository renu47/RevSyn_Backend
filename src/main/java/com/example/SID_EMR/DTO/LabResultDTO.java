package com.example.SID_EMR.DTO;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class LabResultDTO {
    private String testName;
    private String resultValue;
    private String units;
    private LocalDateTime date;
    private String clinician;
    private Long patient;
}
