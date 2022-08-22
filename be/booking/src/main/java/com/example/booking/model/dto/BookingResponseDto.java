package com.example.booking.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class BookingResponseDto implements Serializable {
    private static final long serialVersionUID = 1L;
    private Double latitude;
    private Double longitude;
    private Integer count;
}
