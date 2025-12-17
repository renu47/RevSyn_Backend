package com.example.SID_EMR.Controller;

import com.example.SID_EMR.DTO.AppointmentRequestDTO;
import com.example.SID_EMR.DTO.AppointmentResponseDTO;
import com.example.SID_EMR.Entity.Appointment;
import com.example.SID_EMR.Service.AppointmentService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/appointments")
public class AppointmentController {

    private final AppointmentService appointmentService;

    public AppointmentController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    // ------------------- Schedule Appointment -------------------
    @PostMapping("/add")
    @ResponseStatus(HttpStatus.CREATED)
    public AppointmentResponseDTO scheduleAppointment(@RequestBody AppointmentRequestDTO dto) {
        Appointment appointment = appointmentService.scheduleAppointment(dto);
        return mapToResponse(appointment);
    }

    // ------------------- Update Appointment -------------------
    @PutMapping("/update/{id}")
    public AppointmentResponseDTO updateAppointment(@PathVariable Long id, @RequestBody AppointmentRequestDTO dto) {
        Appointment updated = appointmentService.updateAppointment(id, dto);
        return mapToResponse(updated);
    }

    // ------------------- Cancel Appointment -------------------
    @DeleteMapping("/cancel/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void cancelAppointment(@PathVariable Long id) {
        appointmentService.cancelAppointment(id);
    }

    // ------------------- Get Appointment by ID -------------------
    @GetMapping("/get/{id}")
    public AppointmentResponseDTO getAppointment(@PathVariable Long id) {
        Appointment appointment = appointmentService.getAppointmentById(id);
        return mapToResponse(appointment);
    }

    // ------------------- Get All Appointments -------------------
    @GetMapping("/getall")
    public List<AppointmentResponseDTO> getAllAppointments() {
        return appointmentService.getAllAppointments()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // ------------------- Get Appointments by Doctor -------------------
    @GetMapping("/by-doctor/{doctorId}")
    public List<AppointmentResponseDTO> getAppointmentsByDoctor(@PathVariable Long doctorId) {
        return appointmentService.getAppointmentsByDoctor(doctorId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // ------------------- Get Appointments by Patient -------------------
    @GetMapping("/by-patient/{mobile}")
    public List<AppointmentResponseDTO> getAppointmentsByPatient(@PathVariable String mobile) {
        return appointmentService.getAppointmentsByPatient(mobile)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // ------------------- Get Available Slots for Doctor -------------------
    @GetMapping("/available-slots/{doctorId}")
    public List<String> getAvailableSlots(@PathVariable Long doctorId, @RequestParam String date) {
        // date as yyyy-MM-dd string
        return appointmentService.getAvailableSlots(doctorId, date);
    }

    // ------------------- Mapper -------------------
    private AppointmentResponseDTO mapToResponse(Appointment appointment) {
        return AppointmentResponseDTO.builder()
                .id(appointment.getId())
                .doctorName(appointment.getDoctor().getName())
                .patientName(appointment.getPatientName())
                .patientMobile(appointment.getPatientMobile())
                .ailmentName(appointment.getAilment() != null ? appointment.getAilment().getName() : null)
                .appointmentDate(appointment.getAppointmentDate())
                .appointmentTime(appointment.getAppointmentTime())
                .notes(appointment.getNotes())
                .status(appointment.getStatus().name())
                .consultationFees(appointment.getConsultationFees())
                .paidAmount(appointment.getPaidAmount())
                .createdAt(appointment.getCreatedAt())
                .updatedAt(appointment.getUpdatedAt())
                .build();
    }
}
