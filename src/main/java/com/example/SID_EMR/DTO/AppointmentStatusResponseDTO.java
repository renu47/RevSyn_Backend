package com.example.SID_EMR.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentStatusResponseDTO {

    private long scheduled;
    private long completed;
    private long cancelled;
    private long total;

    public AppointmentStatusResponseDTO(long scheduled, long completed, long cancelled) {
        this.scheduled = scheduled;
        this.completed = completed;
        this.cancelled = cancelled;
        this.total = scheduled + completed + cancelled;
    }
}
