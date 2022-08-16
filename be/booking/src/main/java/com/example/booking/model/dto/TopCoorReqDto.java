package com.example.booking.model.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TopCoorReqDto {
    private Integer userId;
    private String phoneNumber;
    private Integer count;
}