package com.example.SID_EMR.Controller;

import com.example.SID_EMR.DTO.DoctorRequestDTO;
import com.example.SID_EMR.DTO.DoctorResponseDTO;
import com.example.SID_EMR.Entity.Doctor;
import com.example.SID_EMR.Service.DoctorService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/doctors")
public class DoctorController {

    private final DoctorService doctorService;

    public DoctorController(DoctorService doctorService) {
        this.doctorService = doctorService;
    }

    // ------------------- CRUD APIs -------------------

    @PostMapping("/add")
    @ResponseStatus(HttpStatus.CREATED)
    public DoctorResponseDTO createDoctor(@RequestBody DoctorRequestDTO dto) {
        Doctor doctor = mapToEntity(dto);
        Doctor saved = doctorService.createDoctor(doctor, dto.getAilmentIds());
        return mapToResponse(saved);
    }

    @PutMapping("/{id}")
    public DoctorResponseDTO updateDoctor(@PathVariable Long id, @RequestBody DoctorRequestDTO dto) {
        Doctor doctor = mapToEntity(dto);
        Doctor updated = doctorService.updateDoctor(id, doctor, dto.getAilmentIds());
        return mapToResponse(updated);
    }

    @GetMapping("/{id}")
    public DoctorResponseDTO getDoctor(@PathVariable Long id) {
        Doctor doctor = doctorService.getDoctorById(id);
        return mapToResponse(doctor);
    }

    @GetMapping("/getall")
    public List<DoctorResponseDTO> getAllDoctors() {
        return doctorService.getAllActiveDoctors()
                .stream().map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @GetMapping("/by-ailment/{ailmentId}")
    public List<DoctorResponseDTO> getDoctorsByAilment(@PathVariable Long ailmentId) {
        return doctorService.getDoctorsByAilment(ailmentId)
                .stream().map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // ------------------- Prepopulate API -------------------

    @PostMapping("/prepopulate")
    @ResponseStatus(HttpStatus.CREATED)
    public void prepopulateDoctors() {

        // Each doctor with example ailment IDs (adjust according to your DB)
        List<Doctor> doctors = List.of(
            Doctor.builder()
                    .name("Dr. John Smith")
                    .email("john.smith@example.com")
                    .mobile("9999999991")
                    .consultationFees(BigDecimal.valueOf(500))
                    .active(true)
                    .startTime(LocalTime.of(9,0))
                    .endTime(LocalTime.of(17,0))
                    .slotDuration(30)
                    .availableDays(Set.of(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY, DayOfWeek.FRIDAY))
                    .build(),

            Doctor.builder()
                    .name("Dr. Alice Brown")
                    .email("alice.brown@example.com")
                    .mobile("9999999992")
                    .consultationFees(BigDecimal.valueOf(600))
                    .active(true)
                    .startTime(LocalTime.of(10,0))
                    .endTime(LocalTime.of(18,0))
                    .slotDuration(30)
                    .availableDays(Set.of(DayOfWeek.TUESDAY, DayOfWeek.THURSDAY))
                    .build(),

            Doctor.builder()
                    .name("Dr. Michael Green")
                    .email("michael.green@example.com")
                    .mobile("9999999993")
                    .consultationFees(BigDecimal.valueOf(550))
                    .active(true)
                    .startTime(LocalTime.of(8,30))
                    .endTime(LocalTime.of(16,30))
                    .slotDuration(30)
                    .availableDays(Set.of(DayOfWeek.MONDAY, DayOfWeek.THURSDAY))
                    .build(),

            Doctor.builder()
                    .name("Dr. Sarah White")
                    .email("sarah.white@example.com")
                    .mobile("9999999994")
                    .consultationFees(BigDecimal.valueOf(700))
                    .active(true)
                    .startTime(LocalTime.of(11,0))
                    .endTime(LocalTime.of(19,0))
                    .slotDuration(30)
                    .availableDays(Set.of(DayOfWeek.TUESDAY, DayOfWeek.FRIDAY))
                    .build(),

            Doctor.builder()
                    .name("Dr. Robert Black")
                    .email("robert.black@example.com")
                    .mobile("9999999995")
                    .consultationFees(BigDecimal.valueOf(650))
                    .active(true)
                    .startTime(LocalTime.of(9,30))
                    .endTime(LocalTime.of(17,30))
                    .slotDuration(30)
                    .availableDays(Set.of(DayOfWeek.WEDNESDAY, DayOfWeek.THURSDAY))
                    .build()
        );

        // Assign ailments and save each doctor
        List<Set<Long>> doctorAilments = List.of(
            Set.of(1L,2L),
            Set.of(3L,4L),
            Set.of(5L,6L),
            Set.of(7L,8L),
            Set.of(2L,5L)
        );

        for (int i = 0; i < doctors.size(); i++) {
            doctorService.createDoctor(doctors.get(i), doctorAilments.get(i));
        }
    }

    // ------------------- Mapper Methods -------------------

    private DoctorResponseDTO mapToResponse(Doctor doctor) {
        return DoctorResponseDTO.builder()
                .id(doctor.getId())
                .name(doctor.getName())
                .email(doctor.getEmail())
                .mobile(doctor.getMobile())
                .consultationFees(doctor.getConsultationFees())
                .active(doctor.isActive())
                .startTime(doctor.getStartTime())
                .endTime(doctor.getEndTime())
                .slotDuration(doctor.getSlotDuration())
                .availableDays(doctor.getAvailableDays())
                .ailments(doctor.getAilments()
                        .stream()
                        .map(a -> a.getName())
                        .collect(Collectors.toSet()))
                .build();
    }

    private Doctor mapToEntity(DoctorRequestDTO dto) {
        return Doctor.builder()
                .name(dto.getName())
                .email(dto.getEmail())
                .mobile(dto.getMobile())
                .consultationFees(dto.getConsultationFees())
                .active(dto.isActive())
                .startTime(dto.getStartTime())
                .endTime(dto.getEndTime())
                .slotDuration(dto.getSlotDuration())
                .availableDays(dto.getAvailableDays())
                .build();
    }
}
