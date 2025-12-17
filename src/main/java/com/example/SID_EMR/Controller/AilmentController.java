package com.example.SID_EMR.Controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.example.SID_EMR.DTO.AilmentRequestDTO;
import com.example.SID_EMR.DTO.AilmentResponseDTO;
import com.example.SID_EMR.Service.AilmentService;

@RestController
@RequestMapping("/api/ailments")

public class AilmentController {

    private final AilmentService ailmentService;

 // Constructor injection
    public AilmentController(AilmentService ailmentService) {
        this.ailmentService = ailmentService;
    }
    
    @GetMapping("/test")
    public String Welcome() {
        return "Ailment Controller test......";
    }
    
    @PostMapping("/add")
    @ResponseStatus(HttpStatus.CREATED)
    public AilmentResponseDTO createAilment(@RequestBody AilmentRequestDTO dto) {
        return ailmentService.createAilment(dto);
    }

    @GetMapping("/getall")
    public List<AilmentResponseDTO> getAllActiveAilments() {
        return ailmentService.getActiveAilments();
    }
    
    @PostMapping("/prepopulate")
    @ResponseStatus(HttpStatus.CREATED)
    public void prepopulateAilments() {

        List<String> defaultAilments = List.of(
            "GENERAL_PHYSICIAN",
            "CARDIOLOGY",
            "DERMATOLOGY",
            "ORTHOPEDIC",
            "NEUROLOGY",
            "PEDIATRICS",
            "GYNECOLOGY",
            "ENT",
            "OPHTHALMOLOGY",
            "PSYCHIATRY",
            "PULMONOLOGY",
            "GASTROENTEROLOGY",
            "ENDOCRINOLOGY",
            "UROLOGY",
            "NEPHROLOGY",
            "ONCOLOGY",
            "RHEUMATOLOGY",
            "INFECTIOUS_DISEASES",
            "DIABETOLOGY",
            "SPORTS_MEDICINE",
            "PHYSIOTHERAPY"
        );

        defaultAilments.forEach(name -> {
            try {
                ailmentService.createAilment(
                    new com.example.SID_EMR.DTO.AilmentRequestDTO() {{
                        setName(name);
                        setActive(true);
                    }}
                );
            } catch (Exception ignored) {
                // duplicates are intentionally ignored
            }
        });
    }

}
