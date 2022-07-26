package com.amenity_reservation_system.mapper;

import com.amenity_reservation_system.dto.UserDTO;
import com.amenity_reservation_system.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.stream.Collectors;
@Mapper
public interface UserMapper {

    UserMapper MAPPER = Mappers.getMapper(UserMapper.class);

    default List<UserDTO> fromUserList(List<User> users) {
        return  users.stream()
                .map(user -> UserDTO.builder()
                        .id(user.getId())
                        .username(user.getUsername())
                        .password(user.getPassword())
                        .fullName(user.getFullName())
                        .reservations(user.getReservations())
                        .dateCreated(user.getDateCreated())
                        .lastUpdated(user.getLastUpdated())
                        .build())
                .collect(Collectors.toList());
    }

}
