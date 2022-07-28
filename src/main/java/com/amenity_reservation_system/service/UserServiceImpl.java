package com.amenity_reservation_system.service;

import com.amenity_reservation_system.dao.UserRepository;
import com.amenity_reservation_system.dto.UserDTO;
import com.amenity_reservation_system.entity.User;
import com.amenity_reservation_system.mapper.UserMapper;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.ValidationException;
import java.util.List;
import java.util.Objects;


@Service
@Transactional
public class UserServiceImpl implements UserService {

    private final UserMapper MAPPER = UserMapper.MAPPER;

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findFirstByUsername(username);

        if (user == null) {
            throw new UsernameNotFoundException("Такой пользователь не найден: " + username);
        }
        if (user.getUsername().equals("admin"))
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

        if (userDTO.getId()!=null) // При изменении дата создания и пароль сохраняется
            userDTO.setDateCreated(userRepository.getOne(userDTO.getId()).getDateCreated());

        if (userDTO.getId()!=null && userDTO.getPassword().startsWith("$2a"))
            userRepository.save(User.builder()
                    .id(userDTO.getId())
                    .username(userDTO.getUsername())
                    .password(userDTO.getPassword())
                    .fullName(userDTO.getFullName())
                    .dateCreated(userDTO.getDateCreated())
                    .email(userDTO.getEmail())
                    .build());
        else userRepository.save(User.builder()
                .id(userDTO.getId())
                .username(userDTO.getUsername())
                .password(new BCryptPasswordEncoder().encode(userDTO.getPassword()))
                .fullName(userDTO.getFullName())
                .dateCreated(userDTO.getDateCreated())
                .email(userDTO.getEmail())
                .build());
    }

    @Override
    public User findFirstByUsername(String username) {
        return userRepository.findFirstByUsername(username);
    }

    @Override
    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public UserDTO getById(Long id) {
        User user = userRepository.getOne(id);
        return UserDTO.builder()
                .id(user.getId())
                .username(user.getUsername())
                .password(user.getPassword())
                .matchingPassword(user.getPassword())
                .fullName(user.getFullName())
                .dateCreated(user.getDateCreated())
                .email(user.getEmail())
                .build();
    }

    @Override
    public List<UserDTO> findAll() {
        return MAPPER.fromUserList(userRepository.findAll());
    }

}