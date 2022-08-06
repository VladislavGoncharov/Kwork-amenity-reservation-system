package com.amenity_reservation_system.service;

import com.amenity_reservation_system.dao.ReservationRepository;
import com.amenity_reservation_system.dao.UserRepository;
import com.amenity_reservation_system.dto.UserDTO;
import com.amenity_reservation_system.entity.User;
import com.amenity_reservation_system.mapper.UserMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.ValidationException;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


@Service
@Transactional
public class UserServiceImpl implements UserService {

    private final UserMapper MAPPER = UserMapper.MAPPER;

    private final UserRepository userRepository;
    private final ReservationRepository reservationRepository;

    public UserServiceImpl(UserRepository userRepository, ReservationRepository reservationRepository) {
        this.userRepository = userRepository;
        this.reservationRepository = reservationRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findFirstByUsername(username);

        if (user == null) {
            throw new UsernameNotFoundException("Такой пользователь не найден: " + username);
        }

        if (user.getId() == 1)
            return org.springframework.security.core.userdetails.User
                    .withUsername(user.getUsername())
                    .password(user.getPassword())
                    .roles("ADMIN")
                    .build();

        return org.springframework.security.core.userdetails.User
                .withUsername(user.getUsername())
                .password(user.getPassword())
                .roles("USER")
                .build();

    }

    @Override
    public boolean checkUsername(String username) {
        User user = userRepository.findFirstByUsername(username);
        return user != null;
    }

    @Override
    public boolean checkUsername(String username, Long id) {
        User user = userRepository.findFirstByUsername(username);
        if (user == null) return false;
        return !Objects.equals(id, user.getId());
    }

    @Override
    public void updateCheckIn(Long id) {
        User user = userRepository.getOne(id);
        user.setCheckIn(!user.isCheckIn());
        userRepository.save(user);
    }

    @Override
    public void save(UserDTO userDTO) throws ValidationException {

        if (userDTO.getId() != null) { // Валидация имени при изменении
            if (checkUsername(userDTO.getUsername(), userDTO.getId())) {
                throw new ValidationException("A user with this name already exists");

            }
        } else if (checkUsername(userDTO.getUsername())) // При создании
            throw new ValidationException("A user with this name already exists");

        if (!Objects.equals(userDTO.getPassword(), userDTO.getMatchingPassword()))
            throw new ValidationException("Passwords don't match");

        if (userDTO.getEmail().isEmpty()) userDTO.setEmail(null);

        if (userDTO.getId() != null) // При изменении дата создания остается
            userDTO.setDateCreated(userRepository.getOne(userDTO.getId()).getDateCreated());

        User user = User.builder()
                .id(userDTO.getId())
                .username(userDTO.getUsername())
                .password(new BCryptPasswordEncoder().encode(userDTO.getPassword()))
                .fullName(userDTO.getFullName())
                .dateCreated(userDTO.getDateCreated())
                .email(userDTO.getEmail())
                .build();

        if (userDTO.getId() != null && userDTO.getPassword().startsWith("$2a")) {
            user.setPassword(userDTO.getPassword());
            userRepository.save(user);
        } else userRepository.save(user);
    }

    @Override
    public UserDTO findFirstByUsername(String username) {
        return MAPPER.fromUser(userRepository.findFirstByUsername(username));
    }

    @Override
    public void deleteById(Long id) {
        User user = userRepository.getOne(id);
        reservationRepository.deleteAll(user.getReservations());
        userRepository.deleteById(id);
    }

    @Override
    public UserDTO getById(Long id) {
        return MAPPER.fromUser(userRepository.getOne(id));

    }

    @Override
    public List<UserDTO> findAll() {
        return MAPPER.fromUserList(userRepository.findAll()).stream()
                .sorted((o1, o2) -> {
                    if (o1.getId() < (o2.getId())) return 1;
                    return -1;
                })
                .collect(Collectors.toList());
    }

}