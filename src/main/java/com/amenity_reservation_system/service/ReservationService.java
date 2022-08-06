package com.amenity_reservation_system.service;

import com.amenity_reservation_system.dto.ChooseDateAndTime;
import com.amenity_reservation_system.dto.ReservationDTO;
import com.amenity_reservation_system.entity.AmenityType;
import com.amenity_reservation_system.entity.Reservation;

import java.util.List;

public interface ReservationService {

    List<ReservationDTO> findAll();

    List<ReservationDTO> getAllByAmenityType(AmenityType amenityType);

    Reservation findById(Long id);

    void save(ReservationDTO reservationDTO, ChooseDateAndTime chooseDateAndTime);

    void update(Long id, ChooseDateAndTime chooseDateAndTime);

    void deleteById(Long id);
}
