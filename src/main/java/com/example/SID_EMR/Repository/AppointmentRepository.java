package com.example.SID_EMR.Repository;

import com.example.SID_EMR.Entity.Appointment;
import com.example.SID_EMR.Entity.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
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
}
