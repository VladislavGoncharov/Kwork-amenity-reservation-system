package com.amenity_reservation_system.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AmenityTypeDTO {

    private Long id;
    private String amenityName;
    private int capacity;
    private String urlPhoto;

}
