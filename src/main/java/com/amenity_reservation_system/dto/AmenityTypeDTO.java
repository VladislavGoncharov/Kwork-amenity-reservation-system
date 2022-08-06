package com.amenity_reservation_system.dto;

import com.amenity_reservation_system.util.JSONReader;
import lombok.*;

import java.io.IOException;

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
    private String apiTemperature;
    private String temperature;

    public static String getTemperatureByApi(String apiTemperature) {
        try {
            return JSONReader.getTemperatureByJSON(apiTemperature);
        } catch (IOException e) {
            return null;
        }
    }

}
