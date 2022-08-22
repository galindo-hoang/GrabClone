package com.example.booking.model.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TopLocationRequestDto {
    private String phoneNumber;
    private Integer limit;
}
