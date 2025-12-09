package com.example.SID_EMR.Controller;



import com.example.SID_EMR.DTO.PatientRequestDTO;
import com.example.SID_EMR.DTO.UserDTO;
import com.example.SID_EMR.Entity.Patient;
import com.example.SID_EMR.Entity.User;
import com.example.SID_EMR.Service.PatientService;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor 
//@PreAuthorize("hasRole('ADMIN')")
public class PatientController {
	
	@Autowired
    private PatientService emrService;

    @PostMapping("/patients")
    public ResponseEntity<Patient> createPatient(@RequestBody PatientRequestDTO dto,
                                                 @RequestParam String performedBy) {
        Patient created = emrService.createPatient(dto, performedBy);
        return ResponseEntity.ok(created);
    }

    @GetMapping("/patients")
    public ResponseEntity<Patient> getPatient(@RequestParam Long id) {
        Patient patient = emrService.getPatient(id);
        return ResponseEntity.ok(patient);
    }
}


