package com.example.SID_EMR.Service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.SID_EMR.DTO.PatientRequestDTO;
import com.example.SID_EMR.DTO.UserDTO;
import com.example.SID_EMR.Entity.AuditLog;
import com.example.SID_EMR.Entity.LabResult;
import com.example.SID_EMR.Entity.Medication;
import com.example.SID_EMR.Entity.Patient;
import com.example.SID_EMR.Entity.User;
import com.example.SID_EMR.Entity.Visit;
import com.example.SID_EMR.Exception.BadRequestException;
import com.example.SID_EMR.Repository.AuditLogRepository;
import com.example.SID_EMR.Repository.LabResultRepository;
import com.example.SID_EMR.Repository.MedicationRepository;
import com.example.SID_EMR.Repository.PatientRepository;
import com.example.SID_EMR.Repository.UserRepository;
import com.example.SID_EMR.Repository.VisitRepository;
import com.example.SID_EMR.Util.Logging;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PatientService {
	@Autowired
	private  UserRepository userRepository;
	@Autowired
    private  PatientRepository patientRepository;
	@Autowired
    private  VisitRepository visitRepository;
	@Autowired
    private  MedicationRepository medicationRepository;
	@Autowired
    private  LabResultRepository labResultRepository;
	@Autowired
    private  AuditLogRepository auditLogRepository;
	@Autowired
	private Logging log;

    
    @Transactional
    public Patient createPatient(PatientRequestDTO dto, String performedBy) {
        if (dto.getFirstName() == null || dto.getLastName() == null) {
            throw new BadRequestException("Patient first name and last name are required");
        }
        
        Patient patient = Patient.builder()
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .dob(dto.getDob())
                .gender(dto.getGender())
                .contact(dto.getContact())
                .address(dto.getAddress())
                .visits(dto.getVisits())
                .labResults(dto.getLabResults())
                .medications(dto.getMedications())
                .build();

        patientRepository.save(patient);

       

        /*// Visit with diagnoses
        if (dto.getVisits() != null && !dto.getVisits().isEmpty()) {
        	List<Visit> v = dto.getVisits();
            Visit visit = Visit.builder()
                    .patient(patient)
                    .visitDate(LocalDateTime.now())
                    .clinician(performedBy)
                    .reason("Initial Visit")
                    .diagnosis(dto.getVisits().get(0).getDiagnosis())
                    .notes("Auto-created visit")
                    .build();
            visitRepository.save(visit);
        }

        // Medications
        if (dto.getMedications() != null) {
            dto.getMedications().forEach(medDto -> {
                Medication med = Medication.builder()
                        .patient(patient)
                        .name(medDto.getName())
                        .dose(medDto.getDose())
                        .startDate(medDto.getStartDate())
                        .endDate(medDto.getEndDate())
                        .prescriber(medDto.getPrescriber())
                        .build();
                medicationRepository.save(med);
            });
        }

        // Lab Results
        if (dto.getLabResults() != null) {
            dto.getLabResults().forEach(labDto -> {
                LabResult lab = LabResult.builder()
                        .patient(patient)
                        .testName(labDto.getTestName())
                        .resultValue(labDto.getResultValue())
                        .units(labDto.getUnits())
                        .date(labDto.getDate())
                        .clinician(labDto.getClinician())
                        .build();
                labResultRepository.save(lab);
            });
        }
*/
        
        log.logAction("Patient", patient.getId(), "CREATE", performedBy,
                "Patient created with visits, medications, and lab results");

        return patient;
    }

   

	public Patient getPatient(Long id) {
		Patient patient = null;
		if(patientRepository.findById(id) != null)
		{
			 patient = patientRepository.findPatientWithDetails(id).get();
		}
		
		return patient;
	}
}
