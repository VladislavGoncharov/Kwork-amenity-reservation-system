package com.amenity_reservation_system.configration;

import com.amenity_reservation_system.dao.AmenityTypeRepository;
import com.amenity_reservation_system.dao.ReservationRepository;
import com.amenity_reservation_system.dao.UserRepository;
import com.amenity_reservation_system.entity.AmenityType;
import com.amenity_reservation_system.entity.Reservation;
import com.amenity_reservation_system.entity.User;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.time.LocalTime;

@Configuration
public class UploadDataToDB {

    private final String urlDefaultPhoto;
    private final PasswordEncoder passwordEncoder;

    public UploadDataToDB(String urlDefaultPhoto, PasswordEncoder passwordEncoder) {
        this.urlDefaultPhoto = urlDefaultPhoto;
        this.passwordEncoder = passwordEncoder;
    }

    @Bean
    public CommandLineRunner loadData(UserRepository userRepository, AmenityTypeRepository amenityTypeRepository,
                                      ReservationRepository reservationRepository) {
        return (args) -> {
            userRepository.save(User.builder()
                    .username("vlad")
                    .password(passwordEncoder.encode("12345"))
                    .fullName("Vlad Goncharov")
                    .build());
            userRepository.save(User.builder()
                    .username("admin")
                    .password(passwordEncoder.encode("admin753"))
                    .fullName("Main Supervisor")
                    .build());

            amenityTypeRepository.save(AmenityType.builder()
                    .amenityName("GYM")
                    .capacity(20)
                    .urlPhoto(urlDefaultPhoto)
                    .build());
            amenityTypeRepository.save(AmenityType.builder()
                    .amenityName("POOL")
                    .capacity(4)
                    .urlPhoto(urlDefaultPhoto)
                    .build());
            amenityTypeRepository.save(AmenityType.builder()
                    .amenityName("SAUNA")
                    .capacity(1)
                    .urlPhoto(urlDefaultPhoto)
                    .build());

            reservationRepository.save(Reservation.builder()
                    .amenityType(amenityTypeRepository.findFirstByAmenityName("SAUNA"))
                    .reservationDate(LocalDate.now().minusDays(2))
                    .startTime(LocalTime.of(8, 0))
                    .endTime(LocalTime.of(10, 30))
                    .user(userRepository.findFirstByUsername("vlad"))
                    .build());

            reservationRepository.save(Reservation.builder()
                    .amenityType(amenityTypeRepository.findFirstByAmenityName("SAUNA"))
                    .reservationDate(LocalDate.now().minusDays(1))
                    .startTime(LocalTime.of(8, 0))
                    .endTime(LocalTime.of(10, 30))
                    .user(userRepository.findFirstByUsername("vlad"))
                    .build());

            reservationRepository.save(Reservation.builder()
                    .amenityType(amenityTypeRepository.findFirstByAmenityName("SAUNA"))
                    .reservationDate(LocalDate.now())
                    .startTime(LocalTime.of(8, 0))
                    .endTime(LocalTime.of(10, 30))
                    .user(userRepository.findFirstByUsername("vlad"))
                    .build());
            reservationRepository.save(Reservation.builder()
                    .amenityType(amenityTypeRepository.findFirstByAmenityName("SAUNA"))
                    .reservationDate(LocalDate.now().plusDays(1))
                    .startTime(LocalTime.of(8, 0))
                    .endTime(LocalTime.of(10, 30))
                    .user(userRepository.findFirstByUsername("vlad"))
                    .build());
            reservationRepository.save(Reservation.builder()
                    .amenityType(amenityTypeRepository.findFirstByAmenityName("SAUNA"))
                    .reservationDate(LocalDate.now().plusDays(2))
                    .startTime(LocalTime.of(8, 0))
                    .endTime(LocalTime.of(10, 30))
                    .user(userRepository.findFirstByUsername("vlad"))
                    .build());
        };
    }
}
