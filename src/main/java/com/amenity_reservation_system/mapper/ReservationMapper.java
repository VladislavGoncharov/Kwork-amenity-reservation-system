package com.amenity_reservation_system.mapper;

import com.amenity_reservation_system.dto.ReservationDTO;
import com.amenity_reservation_system.entity.Reservation;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.stream.Collectors;

@Mapper
public interface ReservationMapper {

    ReservationMapper MAPPER = Mappers.getMapper(ReservationMapper.class);

    default List<ReservationDTO> fromReservationList(List<Reservation> reservation) {
        return reservation.stream()
                .map(res -> ReservationDTO.builder()
                        .id(res.getId())
                        .reservationDate(res.getReservationDate())
                        .startTime(res.getStartTime())
                        .endTime(res.getEndTime())
                        .amenityType(res.getAmenityType())
                        .user(res.getUser())
                        .build())
                .collect(Collectors.toList());
    }

    default Reservation toReservation(ReservationDTO reservationDTO) {
        return Reservation.builder()
                .id(reservationDTO.getId())
                .reservationDate(reservationDTO.getReservationDate())
                .startTime(reservationDTO.getStartTime())
                .endTime(reservationDTO.getEndTime())
                .amenityType(reservationDTO.getAmenityType())
                .user(reservationDTO.getUser())
                .build();
    }
}
