package com.amenity_reservation_system.dto;

import lombok.*;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDTO {

    private Long id;
    private String username;
    private String fullName;
    private List<ReservationDTO> reservations = new ArrayList<>();

    private String password;
    private String matchingPassword;
    private OffsetDateTime dateCreated;
    private OffsetDateTime lastUpdated;
    private boolean checkIn;
    private String email;

    public String getFormatDateCreated(){
       return this.dateCreated.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
    }

    public List<ReservationDTO> sortReservation(){
        return reservations.stream().sorted((o1, o2) -> {
            if(o1.getReservationDate().isBefore(o2.getReservationDate())) return 1;
            return -1;
        } ).collect(Collectors.toList());
    }
}
