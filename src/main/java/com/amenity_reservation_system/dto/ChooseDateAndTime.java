package com.amenity_reservation_system.dto;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChooseDateAndTime {

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate reservationDate;
    private LocalTime startLocalTime;
    private LocalTime endLocalTime;
    private String startTime;
    private String endTime;

    public void checkTime() {
        int[] startTimeArray = conversionToArrayInt(startTime); //  08:00
        int[] endTimeArray = conversionToArrayInt(endTime);
        startLocalTime = LocalTime.of(startTimeArray[0], startTimeArray[1]);
        endLocalTime = LocalTime.of(endTimeArray[0], endTimeArray[1]);

        if (startLocalTime.isAfter(endLocalTime)) {
            LocalTime time = endLocalTime;
            this.endLocalTime = this.startLocalTime;
            this.startLocalTime = time;
        }
    }

    private int[] conversionToArrayInt(String time) {
        time = time.replaceAll(",", "");
        String[] timeArray = time.split(":");
        return Arrays.stream(timeArray)
                .mapToInt(Integer::parseInt)
                .toArray();
    }


}
