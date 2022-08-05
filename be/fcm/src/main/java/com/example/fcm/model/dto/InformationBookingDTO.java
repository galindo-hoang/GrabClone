package com.example.fcm.model.dto;

/**
 * com.example.fcm.model.dto
 * Created by Admin
 * Date 8/4/2022 - 3:31 PM
 * Description: ...
 */
public class InformationBookingDTO {
    private String fullName;
    private String destination;
    private String departure;
    private String note;
    private Integer version;

    @Override
    public String toString() {
        return "InformationBookingRequestDTO{" +
                "fullName='" + fullName + '\'' +
                ", destination='" + destination + '\'' +
                ", departure='" + departure + '\'' +
                ", note='" + note + '\'' +
                ", version=" + version +
                '}';
    }
}
