package com.amenity_reservation_system.dao;

import com.amenity_reservation_system.entity.AmenityType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AmenityTypeRepository extends JpaRepository<AmenityType, Long> {
    AmenityType findFirstByAmenityName(String amenityName);
}