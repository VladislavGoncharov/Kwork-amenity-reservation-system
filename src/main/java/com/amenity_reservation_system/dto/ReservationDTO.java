package com.amenity_reservation_system.dto;

import com.amenity_reservation_system.entity.AmenityType;
import com.amenity_reservation_system.entity.User;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class ReservationDTO {

    private Long id;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate reservationDate;

    @DateTimeFormat(pattern = "HH:mm")
    private LocalTime startTime;

    @DateTimeFormat(pattern = "HH:mm")
    private LocalTime endTime;
    private User user;
    private String username;
    private AmenityType amenityType;


    public ReservationDTO(String username, AmenityType amenityType) {
        this.username = username;
        this.amenityType = amenityType;
    }

    public ReservationDTO(Long id, AmenityType amenityType) {
        this.id = id;
        this.amenityType = amenityType;
    }

    // Стиль строчек со старыми бронированием
    public String isBookingOld() {
        if (reservationDate.isAfter(LocalDate.now().minusDays(1)))
            return "";
        return "table-light text-secondary";
    }
}
