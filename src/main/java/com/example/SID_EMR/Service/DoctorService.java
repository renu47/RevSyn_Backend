package com.example.SID_EMR.Service;

import com.example.SID_EMR.Entity.Ailment;
import com.example.SID_EMR.Entity.Doctor;
import com.example.SID_EMR.Exception.ResourceNotFoundException;
import com.example.SID_EMR.Repository.AilmentRepository;
import com.example.SID_EMR.Repository.DoctorRepository;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class DoctorService {

    private final DoctorRepository doctorRepository;
    private final AilmentRepository ailmentRepository;

    public DoctorService(DoctorRepository doctorRepository, AilmentRepository ailmentRepository) {
        this.doctorRepository = doctorRepository;
        this.ailmentRepository = ailmentRepository;
    }

    public Doctor createDoctor(Doctor doctor, Set<Long> ailmentIds) {
        Set<Ailment> ailments = new HashSet<>(ailmentRepository.findAllById(ailmentIds));
        doctor.setAilments(ailments);
        return doctorRepository.save(doctor);
    }

    public Doctor updateDoctor(Long doctorId, Doctor doctorDetails, Set<Long> ailmentIds) {
        Doctor existing = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found with id: " + doctorId));

        existing.setName(doctorDetails.getName());
        existing.setEmail(doctorDetails.getEmail());
        existing.setMobile(doctorDetails.getMobile());
        existing.setConsultationFees(doctorDetails.getConsultationFees());
        existing.setActive(doctorDetails.isActive());
        existing.setStartTime(doctorDetails.getStartTime());
        existing.setEndTime(doctorDetails.getEndTime());
        existing.setSlotDuration(doctorDetails.getSlotDuration());
        existing.setAvailableDays(doctorDetails.getAvailableDays());

        Set<Ailment> ailments = new HashSet<>(ailmentRepository.findAllById(ailmentIds));
        existing.setAilments(ailments);

        return doctorRepository.save(existing);
    }

    public Doctor getDoctorById(Long doctorId) {
        return doctorRepository.findById(doctorId)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found with id: " + doctorId));
    }

    public List<Doctor> getAllActiveDoctors() {
        return doctorRepository.findByActiveTrue();
    }

    public List<Doctor> getDoctorsByAilment(Long ailmentId) {
        return doctorRepository.findActiveDoctorsByAilment(ailmentId);
    }
}
