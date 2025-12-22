package com.example.SID_EMR.Repository;

import com.example.SID_EMR.Entity.Appointment;
import com.example.SID_EMR.Entity.AppointmentStatus;
import com.example.SID_EMR.Entity.Doctor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    // Find all appointments for a doctor
    List<Appointment> findByDoctor(Doctor doctor);

    // Find all appointments for a patient
    List<Appointment> findByPatientMobile(String patientMobile);

    // Find appointments for a doctor on a specific date
    List<Appointment> findByDoctorAndAppointmentDate(Doctor doctor, LocalDate appointmentDate);

    // Check if a slot is already booked for a doctor on a specific date & time
    boolean existsByDoctorAndAppointmentDateAndAppointmentTime(
            Doctor doctor,
            LocalDate appointmentDate,
            LocalTime appointmentTime
    );
    @Query("""
            SELECT a FROM Appointment a
            WHERE a.appointmentDate BETWEEN :fromDate AND :toDate
            AND (:patientMobile IS NULL OR a.patientMobile = :patientMobile)
            AND (:doctorId IS NULL OR a.doctor.id = :doctorId)
            AND (:ailmentId IS NULL OR a.ailment.id = :ailmentId)
            AND (:status IS NULL OR a.status = :status)
        """)
        Page<Appointment> searchAppointments(
                LocalDate fromDate,
                LocalDate toDate,
                String patientMobile,
                Long doctorId,
                Long ailmentId,
                AppointmentStatus status,
                Pageable pageable
        );
    
    /* ===================== STATUS COUNT APIs ===================== */

    @Query("""
        SELECT COUNT(a)
        FROM Appointment a
        WHERE a.appointmentDate BETWEEN :fromDate AND :toDate
            AND (:patientMobile IS NULL OR a.patientMobile = :patientMobile)
            AND (:doctorId IS NULL OR a.doctor.id = :doctorId)
            AND (:ailmentId IS NULL OR a.ailment.id = :ailmentId)
            AND (:status IS NULL OR a.status = :status)
    """)
    long countByStatusAndDateRange(
            @Param("status") AppointmentStatus status,
            @Param("fromDate") LocalDate fromDate,
            @Param("toDate") LocalDate toDate,
            String patientMobile,
            Long doctorId,
            Long ailmentId
    );
}
