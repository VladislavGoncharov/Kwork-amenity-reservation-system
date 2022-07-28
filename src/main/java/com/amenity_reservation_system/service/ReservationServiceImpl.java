package com.amenity_reservation_system.service;

import com.amenity_reservation_system.dao.AmenityTypeRepository;
import com.amenity_reservation_system.dao.ReservationRepository;
import com.amenity_reservation_system.dao.UserRepository;
import com.amenity_reservation_system.dto.ChooseDateAndTime;
import com.amenity_reservation_system.dto.ReservationDTO;
import com.amenity_reservation_system.entity.Reservation;
import com.amenity_reservation_system.entity.User;
import com.amenity_reservation_system.mapper.ReservationMapper;
import com.amenity_reservation_system.util.EmailSenderService;
import org.springframework.http.HttpStatus;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;


@Service
@Transactional
public class ReservationServiceImpl implements ReservationService {

    private final ReservationMapper MAPPER = ReservationMapper.MAPPER;

    private final JavaMailSender mailSender;
    private final ReservationRepository reservationRepository;
    private final UserRepository userRepository;
    private final AmenityTypeRepository amenityTypeRepository;

    public ReservationServiceImpl(JavaMailSender mailSender, ReservationRepository reservationRepository, UserRepository userRepository, AmenityTypeRepository amenityTypeRepository) {
        this.mailSender = mailSender;
        this.reservationRepository = reservationRepository;
        this.userRepository = userRepository;
        this.amenityTypeRepository = amenityTypeRepository;
    }

    @Override
    public List<ReservationDTO> findAll() {
        return MAPPER.fromReservationList(
                reservationRepository.findAll().stream()
                        .sorted((o1, o2) -> {
                            if (o1.getReservationDate().isBefore(o2.getReservationDate())) return 1;
                            return -1;
                        })
                        .collect(Collectors.toList()));
    }

    @Override
    public Reservation findById(Long id) {
        return reservationRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @Override
    public void save(ReservationDTO reservationDTO, ChooseDateAndTime chooseDateAndTime) {
        reservationDTO.setReservationDate(chooseDateAndTime.getReservationDate());
        reservationDTO.setStartTime(chooseDateAndTime.getStartLocalTime());
        reservationDTO.setEndTime(chooseDateAndTime.getEndLocalTime());

        Reservation reservation =
                MAPPER.toReservation(
                        reservationDTO,
                        amenityTypeRepository.findFirstByAmenityName(reservationDTO.getAmenityType().getAmenityName()));

        saveOrUpdateReservation(reservation);
    }

    @Override
    public void update(Long id, ChooseDateAndTime chooseDateAndTime) {
        Reservation reservation = reservationRepository.getOne(id);
        reservation.setReservationDate(chooseDateAndTime.getReservationDate());
        reservation.setStartTime(chooseDateAndTime.getStartLocalTime());
        reservation.setEndTime(chooseDateAndTime.getEndLocalTime());

        saveOrUpdateReservation(reservation);
    }
    private void saveOrUpdateReservation(Reservation reservation){
        reservationRepository.save(reservation);

        User user = reservation.getUser();
        user.addReservation(reservation);
        userRepository.save(user);
        if (user.getEmail() != null) {
            Thread thread = new Thread(new EmailSenderService(mailSender, user.getEmail(), reservation));
            thread.start();
        }
    }

    @Override
    public void deleteById(Long id) {
        reservationRepository.deleteById(id);
    }

    @Override
    public void updateCheckIn(Long id) {
        Reservation reservation = reservationRepository.getOne(id);
        User user = userRepository.getOne(reservation.getUser().getId());
        user.setCheckIn(!user.isCheckIn());
        userRepository.save(user);
    }

}