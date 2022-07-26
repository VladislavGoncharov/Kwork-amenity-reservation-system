package com.amenity_reservation_system.dto;

import com.amenity_reservation_system.entity.Reservation;
import com.amenity_reservation_system.entity.User;
import lombok.*;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDTO {

    private Long id;
    private String username;
    private String fullName;
    private List<Reservation> reservations = new ArrayList<>();

    private String password;
    private String matchingPassword;
    private OffsetDateTime dateCreated;
    private OffsetDateTime lastUpdated;

    public UserDTO(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.fullName = user.getFullName();
        this.reservations = user.getReservations();
    }

    public String getFormatDateCreated(){
       return this.dateCreated.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
    }
}
