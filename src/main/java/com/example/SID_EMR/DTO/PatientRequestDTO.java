package com.example.SID_EMR.DTO;

import lombok.Data;
import java.time.LocalDate;
import java.util.List;

import com.example.SID_EMR.Entity.LabResult;
import com.example.SID_EMR.Entity.Medication;
import com.example.SID_EMR.Entity.Visit;

@Data
public class PatientRequestDTO {
    private String firstName;
    private String lastName;
    private LocalDate dob;
    private String gender;
    private String contact;
    private String address;

    private List<Visit> visits;

    private List<Medication> medications;

    private List<LabResult> labResults;
}