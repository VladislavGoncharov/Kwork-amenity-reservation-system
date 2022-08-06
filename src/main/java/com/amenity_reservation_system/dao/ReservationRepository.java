package com.amenity_reservation_system.dao;

import com.amenity_reservation_system.entity.AmenityType;
import com.amenity_reservation_system.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    List<Reservation> getAllByAmenityType(AmenityType amenityType);

}
