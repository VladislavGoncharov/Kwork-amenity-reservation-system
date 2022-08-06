package com.amenity_reservation_system.util;

import com.amenity_reservation_system.dto.ChooseDateAndTime;
import com.amenity_reservation_system.dto.ReservationDTO;
import com.amenity_reservation_system.entity.BookingTimeEnum;
import com.amenity_reservation_system.entity.Reservation;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

public class FreeTime { // Вспомогательный класс, где происходит магия.
    // А если точнее показывается свободное время для резервирования на неделю вперед

    // Используется при изменении резервирования, чтобы освободилось изначально зарезервированное время
    public static List<List<BookingTimeEnum>> updateReservation(List<ReservationDTO> reservation
            , Reservation updateReservation) {
        ReservationDTO deleteUpdateReservation
                = reservation.stream()
                .filter(reservationDTO -> Objects.equals(reservationDTO.getId(), updateReservation.getId()))
                .findFirst().get();

        reservation.remove(deleteUpdateReservation);

        return sortByDate(reservation, updateReservation.getAmenityType().getCapacity());
    }

    public static List<List<BookingTimeEnum>> sortByDate(List<ReservationDTO> reservation, int capacityAmenityType) {
        List<List<BookingTimeEnum>> freeTimeWeekList = new ArrayList<>();

        LocalDate yesterday = LocalDate.now().minusDays(1);

        // Берем в расчет только будущие бронирования
        reservation = reservation.stream()
                .filter(res -> res.getReservationDate().isAfter(yesterday))
                .collect(Collectors.toList());


        // Распределяем свободное время по дням на неделю вперед
        for (int i = 0; i < 7; i++) {
            LocalDate today = LocalDate.now().plusDays(i);

            List<ReservationDTO> currentDate = reservation.stream()
                    .filter(res -> res.getReservationDate().isEqual(today))
                    .collect(Collectors.toList());

            freeTimeWeekList.add(getFreeTime(currentDate, capacityAmenityType));
        }

        return freeTimeWeekList;
    }

    // Высчитываем свободное время на один день
    public static List<BookingTimeEnum> getFreeTime(List<ReservationDTO> reservation, int capacityAmenityType) {

        // Создаю Лист содержащий Множества(Set) всего времени
        List<Set<BookingTimeEnum>> allTimeList = new ArrayList<>();

        // И добавляю Множества времени в лист согласно Вместительности Удобства
        for (int i = 0; i < capacityAmenityType; i++) {
            allTimeList.add(new HashSet<>(List.of(BookingTimeEnum.values())));
        }
        // У меня создается Лист содержащий временные интервалы от 08:00 до 20:00 по количеству Вместительности Удобства

        // Создаю Лист содержащий Множества уже забронированного времени
        List<Set<BookingTimeEnum>> reservedTimeSlots = new ArrayList<>();
        // Прохожу по каждому бронированию и
        // создаю ЗАРЕЗЕРВИРОВАННЫЕ временные интервалы и добавляю их в лист reservedTimeSlots
        for (ReservationDTO res : reservation) {
            Set<BookingTimeEnum> reservedTimeSlot = new HashSet<>();
            LocalTime startTime = res.getStartTime();

            while (startTime.isBefore(res.getEndTime())) {
                BookingTimeEnum timeEnum = BookingTimeEnum.getBookingTimeEnum(startTime.toString());
                reservedTimeSlot.add(timeEnum);
                startTime = startTime.plusMinutes(30);
            }
            reservedTimeSlot.add(BookingTimeEnum.getBookingTimeEnum(res.getEndTime().toString()));

            reservedTimeSlots.add(reservedTimeSlot);
        }
        // Объясняю что произошло в forEach. Допустим я зарезервировал Зал с 16:00 до 18:00
        // В forEach я создал Set и добавил в него все временные промежутки с 16:00 до 18:00
        // А именно 16:00, 16:30, 17:00, 17:30, 18:00.
        // Далее добавил временный Set в Лист reservedTimeSlots и я получил все занятые временные промежутки в Одном листе

        for (Set<BookingTimeEnum> reservedTimeSlot : reservedTimeSlots) {

            for (Set<BookingTimeEnum> allTime : allTimeList) {
                if (allTime.containsAll(reservedTimeSlot)) {
                    allTime.removeAll(reservedTimeSlot);
                    break;
                }
            }
        }

        Set<BookingTimeEnum> freeTime = new HashSet<>();

        for (Set<BookingTimeEnum> allTime : allTimeList) {
            freeTime.addAll(allTime);
        }

        return freeTime.stream().sorted().collect(Collectors.toList());
    }

    public static boolean checkingForAvailableSeats(Object bookingTimeEnumList, ChooseDateAndTime chooseDateAndTime) {
        LocalDate specificDate = chooseDateAndTime.getReservationDate();
        LocalTime startTime = chooseDateAndTime.getStartLocalTime();
        LocalTime endTime = chooseDateAndTime.getEndLocalTime();

        LocalDate now = LocalDate.now();

        int i = 0;
        while (!now.isEqual(specificDate)) {
            i++;
            now = now.plusDays(1);
        }

        List<List<BookingTimeEnum>> bookingTimeEnum = (List<List<BookingTimeEnum>>) bookingTimeEnumList;
        List<BookingTimeEnum> specificDateList = bookingTimeEnum.get(i);

        while (startTime.isBefore(endTime)) {
            BookingTimeEnum time = BookingTimeEnum.getBookingTimeEnum(startTime.toString());

            if (!specificDateList.contains(time)) return true;
            startTime = startTime.plusMinutes(30);
        }
        return false;
    }
}
