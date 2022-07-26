package com.amenity_reservation_system.service;

import com.amenity_reservation_system.dto.UserDTO;
import com.amenity_reservation_system.entity.User;
import org.springframework.security.core.userdetails.UserDetailsService;

import javax.validation.ValidationException;
import java.util.List;

public interface UserService extends UserDetailsService {

    List<UserDTO> findAll();

    User findFirstByUsername(String name);

    void save(UserDTO userDTO) throws ValidationException;

    void deleteById(Long id);

    UserDTO getById(Long id);

    boolean checkUsername(String username);

    boolean checkUsername(String username, Long id);

}
