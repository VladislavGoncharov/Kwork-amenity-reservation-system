package com.amenity_reservation_system.entity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public enum BookingTimeEnum {
    EIGHT("08:00"),
    EIGHT30("08:30"),
    NINE("09:00"),
    NINE30("09:30"),
    TEN("10:00"),
    TEN30("10:30"),
    ELEVEN("11:00"),
    ELEVEN30("11:30"),
    TWELVE("12:00"),
    TWELVE30("12:30"),
    THIRTEEN("13:00"),
    THIRTEEN30("13:30"),
    FOURTEEN("14:00"),
    FOURTEEN30("14:30"),
    FIFTEEN("15:00"),
    FIFTEEN30("15:30"),
    SIXTEEN("16:00"),
    SIXTEEN30("16:30"),
    SEVENTEEN("17:00"),
    SEVENTEEN30("17:30"),
    EIGHTEEN("18:00"),
    EIGHTEEN30("18:30"),
    NINETEEN("19:00"),
    NINETEEN30("19:30"),
    TWENTY("20:00");

    private final String time;

    BookingTimeEnum(String time) {
        this.time = time;
    }

    public String getTime() {
        return time;
    }

    public static BookingTimeEnum getBookingTimeEnum(String time) {
        return Arrays.stream(BookingTimeEnum.values())
                .filter(bookingTimeEnum -> bookingTimeEnum.getTime().equals(time))
                .findFirst()
                .get();
    }

    public static List<BookingTimeEnum> startList(List<BookingTimeEnum> list) {
        List<BookingTimeEnum> timeEnumList = new ArrayList<>(list);
        timeEnumList.remove(TWENTY);
        return timeEnumList;
    }

    public static List<BookingTimeEnum> endList(List<BookingTimeEnum> list) {
        List<BookingTimeEnum> timeEnumList = new ArrayList<>(list);
        timeEnumList.remove(EIGHT);
        return timeEnumList;
    }

}
