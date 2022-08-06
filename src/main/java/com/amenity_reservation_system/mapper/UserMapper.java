package com.amenity_reservation_system.mapper;

import com.amenity_reservation_system.dto.ReservationDTO;
import com.amenity_reservation_system.dto.UserDTO;
import com.amenity_reservation_system.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.stream.Collectors;

@Mapper
public interface UserMapper {

    UserMapper MAPPER = Mappers.getMapper(UserMapper.class);

    default UserDTO fromUser(User user) {
        return UserDTO.builder()
                .id(user.getId())
                .username(user.getUsername())
                .password(user.getPassword())
                .matchingPassword(user.getPassword())
                .fullName(user.getFullName())
                .reservations(user.getReservations().stream()
                        .map(res -> ReservationDTO.builder()
                                .id(res.getId())
                                .user(res.getUser())
                                .amenityType(res.getAmenityType())
                                .reservationDate(res.getReservationDate())
                                .startTime(res.getStartTime())
                                .endTime(res.getEndTime())
                                .build())
                        .collect(Collectors.toList()))
                .dateCreated(user.getDateCreated())
                .lastUpdated(user.getLastUpdated())
                .checkIn(user.isCheckIn())
                .email(user.getEmail())
                .build();
    }

    default List<UserDTO> fromUserList(List<User> users) {
        return users.stream()
                .map(this::fromUser)
                .collect(Collectors.toList());
    }

}
