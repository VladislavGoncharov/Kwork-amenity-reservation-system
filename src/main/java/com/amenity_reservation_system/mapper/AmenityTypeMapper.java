package com.amenity_reservation_system.mapper;

import com.amenity_reservation_system.dto.AmenityTypeDTO;
import com.amenity_reservation_system.entity.AmenityType;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.stream.Collectors;

@Mapper
public interface AmenityTypeMapper {

    AmenityTypeMapper MAPPER = Mappers.getMapper(AmenityTypeMapper.class);

    default List<AmenityTypeDTO> fromAmenityTypeList(List<AmenityType> amenityTypes) {
        return amenityTypes.stream()
                .map(amenityType -> AmenityTypeDTO.builder()
                        .id(amenityType.getId())
                        .amenityName(amenityType.getAmenityName())
                        .capacity(amenityType.getCapacity())
                        .urlPhoto(amenityType.getUrlPhoto())
                        .apiTemperature(amenityType.getApiTemperature())
                        .temperature(AmenityTypeDTO.getTemperatureByApi(amenityType.getApiTemperature()))
                        .build())
                .collect(Collectors.toList());
    }

    default AmenityTypeDTO fromAmenityType(AmenityType amenityType) {
        return AmenityTypeDTO.builder()
                .id(amenityType.getId())
                .amenityName(amenityType.getAmenityName())
                .capacity(amenityType.getCapacity())
                .urlPhoto(amenityType.getUrlPhoto())
                .apiTemperature(amenityType.getApiTemperature())
                .temperature(AmenityTypeDTO.getTemperatureByApi(amenityType.getApiTemperature()))
                .build();
    }
}
