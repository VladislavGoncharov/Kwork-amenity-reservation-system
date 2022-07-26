package com.amenity_reservation_system.service;

import com.amenity_reservation_system.dto.AmenityTypeDTO;
import com.amenity_reservation_system.entity.AmenityType;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.ValidationException;
import java.io.IOException;
import java.util.List;

public interface AmenityTypeService {

    List<AmenityTypeDTO> findAll();

    AmenityType findFirstByAmenityName(String amenityName);

    void save(AmenityTypeDTO amenityTypeDTO, MultipartFile photo) throws IOException, ValidationException;

    void deleteById(Long id);

    AmenityTypeDTO getById(Long id);

    boolean checkUsername(String amenityName);

    boolean checkUsername(String amenityName, Long id);

}
