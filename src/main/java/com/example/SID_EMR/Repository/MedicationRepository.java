package com.example.SID_EMR.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.SID_EMR.Entity.Medication;

public interface MedicationRepository extends JpaRepository<Medication, Long> {
}
