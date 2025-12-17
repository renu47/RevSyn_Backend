package com.example.SID_EMR.Entity;

import jakarta.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.Set;

@Entity
@Table(name = "doctors")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Doctor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    private String mobile;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "doctor_ailments",
        joinColumns = @JoinColumn(name = "doctor_id"),
        inverseJoinColumns = @JoinColumn(name = "ailment_id")
    )
    private Set<Ailment> ailments;

    private BigDecimal consultationFees;

    private boolean active;

    private LocalTime startTime;

    private LocalTime endTime;

    /**
     * Slot duration in minutes (e.g. 15, 30)
     */
    private Integer slotDuration;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
        name = "doctor_available_days",
        joinColumns = @JoinColumn(name = "doctor_id")
    )
    @Enumerated(EnumType.STRING)
    @Column(name = "available_day")
    private Set<DayOfWeek> availableDays;
}
