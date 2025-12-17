package com.example.SID_EMR.Repository;

import com.example.SID_EMR.Entity.Ailment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AilmentRepository extends JpaRepository<Ailment, Long> {

    Optional<Ailment> findByNameIgnoreCase(String name);

    List<Ailment> findByActiveTrue();
}
