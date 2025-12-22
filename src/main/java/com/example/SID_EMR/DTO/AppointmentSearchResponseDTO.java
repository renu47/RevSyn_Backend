package com.example.SID_EMR.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.Page;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentSearchResponseDTO {

    private Page<AppointmentListResponseDTO> appointments;
    private AppointmentStatusResponseDTO statusCounts;
}
