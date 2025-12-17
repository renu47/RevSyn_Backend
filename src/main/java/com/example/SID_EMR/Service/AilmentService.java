package com.example.SID_EMR.Service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.SID_EMR.DTO.AilmentRequestDTO;
import com.example.SID_EMR.DTO.AilmentResponseDTO;
import com.example.SID_EMR.Entity.Ailment;
import com.example.SID_EMR.Exception.BadRequestException;
import com.example.SID_EMR.Exception.ResourceNotFoundException;
import com.example.SID_EMR.Repository.AilmentRepository;

@Service
public class AilmentService {

    private final AilmentRepository ailmentRepository;
    
 // Explicit constructor injection
    public AilmentService(AilmentRepository ailmentRepository) {
        this.ailmentRepository = ailmentRepository;
    }

    public AilmentResponseDTO createAilment(AilmentRequestDTO dto) {

        ailmentRepository.findByNameIgnoreCase(dto.getName())
                .ifPresent(a -> {
                    throw new BadRequestException("Ailment already exists: " + dto.getName());
                });

        Ailment ailment = Ailment.builder()
                .name(dto.getName())
                .active(dto.isActive())
                .build();

        return mapToResponse(ailmentRepository.save(ailment));
    }

    public List<AilmentResponseDTO> getActiveAilments() {

        List<Ailment> ailments = ailmentRepository.findByActiveTrue();

        if (ailments.isEmpty()) {
            throw new ResourceNotFoundException("No active ailments found");
        }

        return ailments.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public Ailment getAilmentById(Long id) {
        return ailmentRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Ailment not found with id: " + id)
                );
    }

    private AilmentResponseDTO mapToResponse(Ailment ailment) {
        return AilmentResponseDTO.builder()
                .id(ailment.getId())
                .name(ailment.getName())
                .active(ailment.isActive())
                .build();
    }
}
