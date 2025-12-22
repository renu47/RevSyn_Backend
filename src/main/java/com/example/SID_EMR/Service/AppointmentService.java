package com.example.SID_EMR.Service;

import com.example.SID_EMR.DTO.AppointmentListResponseDTO;
import com.example.SID_EMR.DTO.AppointmentRequestDTO;
import com.example.SID_EMR.DTO.AppointmentStatusResponseDTO;
import com.example.SID_EMR.Entity.Ailment;
import com.example.SID_EMR.Entity.Appointment;
import com.example.SID_EMR.Entity.AppointmentStatus;
import com.example.SID_EMR.Entity.Doctor;
import com.example.SID_EMR.Exception.BadRequestException;
import com.example.SID_EMR.Exception.ResourceNotFoundException;
import com.example.SID_EMR.Repository.AilmentRepository;
import com.example.SID_EMR.Repository.AppointmentRepository;
import com.example.SID_EMR.Repository.DoctorRepository;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final DoctorRepository doctorRepository;
    private final AilmentRepository ailmentRepository;

    public AppointmentService(AppointmentRepository appointmentRepository,
                              DoctorRepository doctorRepository,
                              AilmentRepository ailmentRepository) {
        this.appointmentRepository = appointmentRepository;
        this.doctorRepository = doctorRepository;
        this.ailmentRepository = ailmentRepository;
    }

    // ------------------- Schedule Appointment -------------------
    public Appointment scheduleAppointment(AppointmentRequestDTO dto) {

        Doctor doctor = doctorRepository.findById(dto.getDoctorId())
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found with id " + dto.getDoctorId()));

        Ailment ailment = ailmentRepository.findById(dto.getAilmentId())
                .orElseThrow(() -> new ResourceNotFoundException("Ailment not found with id " + dto.getAilmentId()));

        // Check if doctor is active
        if (!doctor.isActive()) {
            throw new BadRequestException("Doctor is not active");
        }

        // Check if doctor works on that day
        if (!doctor.getAvailableDays().contains(dto.getAppointmentDate().getDayOfWeek())) {
            throw new BadRequestException("Doctor is not available on " + dto.getAppointmentDate().getDayOfWeek());
        }

        // Check if appointment time is within working hours
        if (dto.getAppointmentTime().isBefore(doctor.getStartTime()) ||
            dto.getAppointmentTime().isAfter(doctor.getEndTime().minusMinutes(doctor.getSlotDuration()))) {
            throw new BadRequestException("Appointment time is outside doctor's working hours");
        }

        // Check if slot is already taken
        boolean exists = appointmentRepository.existsByDoctorAndAppointmentDateAndAppointmentTime(
                doctor, dto.getAppointmentDate(), dto.getAppointmentTime()
        );
        if (exists) {
            throw new BadRequestException("Selected time slot is already booked");
        }

        Appointment appointment = Appointment.builder()
                .doctor(doctor)
                .patientName(dto.getPatientName())
                .patientMobile(dto.getPatientMobile())
                .ailment(ailment)
                .appointmentDate(dto.getAppointmentDate())
                .appointmentTime(dto.getAppointmentTime())
                .notes(dto.getNotes())
                .consultationFees(doctor.getConsultationFees())
                .status(AppointmentStatus.SCHEDULED)
                .build();

        return appointmentRepository.save(appointment);
    }

    // ------------------- Update Appointment -------------------
    public Appointment updateAppointment(Long id, AppointmentRequestDTO dto) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment not found with id " + id));

        // Only allow updates to patient info, notes, and time
        appointment.setPatientName(dto.getPatientName());
        appointment.setPatientMobile(dto.getPatientMobile());
        appointment.setNotes(dto.getNotes());

        // Optionally reschedule
        if (dto.getAppointmentDate() != null && dto.getAppointmentTime() != null) {
            scheduleAppointmentCheck(appointment.getDoctor(), dto.getAppointmentDate(), dto.getAppointmentTime());
            appointment.setAppointmentDate(dto.getAppointmentDate());
            appointment.setAppointmentTime(dto.getAppointmentTime());
        }

        return appointmentRepository.save(appointment);
    }

    // Helper to check availability for update
    private void scheduleAppointmentCheck(Doctor doctor, LocalDate date, LocalTime time) {
        if (!doctor.getAvailableDays().contains(date.getDayOfWeek())) {
            throw new BadRequestException("Doctor is not available on " + date.getDayOfWeek());
        }
        if (time.isBefore(doctor.getStartTime()) || time.isAfter(doctor.getEndTime().minusMinutes(doctor.getSlotDuration()))) {
            throw new BadRequestException("Appointment time is outside doctor's working hours");
        }
        boolean exists = appointmentRepository.existsByDoctorAndAppointmentDateAndAppointmentTime(doctor, date, time);
        if (exists) {
            throw new BadRequestException("Selected time slot is already booked");
        }
    }

    // ------------------- Cancel Appointment -------------------
    public void cancelAppointment(Long id) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment not found with id " + id));
        appointment.setStatus(AppointmentStatus.CANCELLED);
        appointmentRepository.save(appointment);
    }

    // ------------------- Get Appointment by ID -------------------
    public Appointment getAppointmentById(Long id) {
        return appointmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment not found with id " + id));
    }

    // ------------------- Get All Appointments -------------------
    public List<Appointment> getAllAppointments() {
        return appointmentRepository.findAll();
    }

    // ------------------- Get Appointments by Doctor -------------------
    public List<Appointment> getAppointmentsByDoctor(Long doctorId) {
        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found with id " + doctorId));
        return appointmentRepository.findByDoctor(doctor);
    }

    // ------------------- Get Appointments by Patient -------------------
    public List<Appointment> getAppointmentsByPatient(String patientMobile) {
        return appointmentRepository.findByPatientMobile(patientMobile);
    }

    // ------------------- Get Available Slots -------------------
    public List<String> getAvailableSlots(Long doctorId, String dateStr) {
        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found with id " + doctorId));

        LocalDate date = LocalDate.parse(dateStr, DateTimeFormatter.ISO_DATE);

        if (!doctor.getAvailableDays().contains(date.getDayOfWeek())) {
            return Collections.emptyList();
        }

        List<LocalTime> slots = new ArrayList<>();
        LocalTime current = doctor.getStartTime();
        while (!current.isAfter(doctor.getEndTime().minusMinutes(doctor.getSlotDuration()))) {
            slots.add(current);
            current = current.plusMinutes(doctor.getSlotDuration());
        }

        // Remove already booked slots
        List<LocalTime> bookedSlots = appointmentRepository
                .findByDoctorAndAppointmentDate(doctor, date)
                .stream()
                .map(Appointment::getAppointmentTime)
                .collect(Collectors.toList());

        slots.removeAll(bookedSlots);

        // Return as string "HH:mm"
        return slots.stream()
                .map(t -> t.toString())
                .collect(Collectors.toList());
    }
    
    public Page<AppointmentListResponseDTO> getAppointments(
            LocalDate fromDate,
            LocalDate toDate,
            String patientMobile,
            Long doctorId,
            Long ailmentId,
            String status,
            int page,
            int size
    ) {

        if (fromDate.isAfter(toDate)) {
            throw new IllegalArgumentException("fromDate cannot be after toDate");
        }
        if (patientMobile != null && patientMobile.isBlank()) {
            patientMobile = null;
        }
        if (doctorId != null && doctorId == 0) {
            patientMobile = null;
        }
        if (ailmentId != null && ailmentId == 0) {
        	ailmentId = null;
        }
        AppointmentStatus Astatus = null;
        if(status.trim().equalsIgnoreCase("SCHEDULED"))
        	Astatus = AppointmentStatus.SCHEDULED;
        else if(status.trim().equalsIgnoreCase("COMPLETED"))
        	Astatus = AppointmentStatus.COMPLETED;
        else if(status.trim().equalsIgnoreCase("CANCELLED"))
        	Astatus = AppointmentStatus.CANCELLED;
        else 
        	Astatus = null;
        

        Pageable pageable =
                PageRequest.of(page, size, Sort.by("appointmentDate").ascending());

        Page<Appointment> appointments =
                appointmentRepository.searchAppointments(
                        fromDate,
                        toDate,
                        patientMobile,
                        doctorId,
                        ailmentId,
                        Astatus,
                        pageable
                );

        return appointments.map(a -> new AppointmentListResponseDTO(
                a.getId(),
                a.getPatientName(),
                a.getPatientMobile(),
                a.getDoctor().getId(),
                a.getDoctor().getName(),
                a.getAilment().getId(),
                a.getAilment().getName(),
                a.getAppointmentDate(),
                a.getAppointmentTime(),
                a.getConsultationFees(),
                a.getStatus()
        ));
    }
    
    public AppointmentStatusResponseDTO getAppointmentStats(LocalDate fromDate,
            LocalDate toDate,
            String patientMobile,
            Long doctorId,
            Long ailmentId) {

        if (fromDate.isAfter(toDate)) {
            throw new IllegalArgumentException("fromDate cannot be after toDate");
        }
        if (patientMobile != null && patientMobile.isBlank()) {
            patientMobile = null;
        }
        if (doctorId != null && doctorId == 0) {
            patientMobile = null;
        }
        if (ailmentId != null && ailmentId == 0) {
        	ailmentId = null;
        }


        long scheduled = appointmentRepository.countByStatusAndDateRange(
                AppointmentStatus.SCHEDULED,
                fromDate, 
                toDate,          
                patientMobile,
                doctorId,
                ailmentId
        );

        long completed = appointmentRepository.countByStatusAndDateRange(
                AppointmentStatus.COMPLETED,
                fromDate, 
                toDate,          
                patientMobile,
                doctorId,
                ailmentId
        );

        long cancelled = appointmentRepository.countByStatusAndDateRange(
                AppointmentStatus.CANCELLED, 
                fromDate, 
                toDate,          
                patientMobile,
                doctorId,
                ailmentId
        );

        return new AppointmentStatusResponseDTO(scheduled, completed, cancelled);
    }

}
