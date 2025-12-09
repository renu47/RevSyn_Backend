package com.example.SID_EMR.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.SID_EMR.Entity.Patient;

public interface PatientRepository extends JpaRepository<Patient, Long> {

	@Query("SELECT p FROM Patient p " +
       "LEFT JOIN FETCH p.visits " +
       "WHERE p.id = :id")
	Optional<Patient> findPatientWithDetails(@Param("id") Long id);
	
}
