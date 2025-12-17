package com.example.SID_EMR.Repository;

import com.example.SID_EMR.Entity.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DoctorRepository extends JpaRepository<Doctor, Long> {

    @Query("""
        SELECT DISTINCT d
        FROM Doctor d
        JOIN d.ailments a
        WHERE a.id = :ailmentId
        AND d.active = true
    """)
    List<Doctor> findActiveDoctorsByAilment(@Param("ailmentId") Long ailmentId);

    List<Doctor> findByActiveTrue();
}
