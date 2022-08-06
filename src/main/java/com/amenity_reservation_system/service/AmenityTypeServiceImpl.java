package com.amenity_reservation_system.service;

import com.amenity_reservation_system.dao.AmenityTypeRepository;
import com.amenity_reservation_system.dao.ReservationRepository;
import com.amenity_reservation_system.dao.UserRepository;
import com.amenity_reservation_system.dto.AmenityTypeDTO;
import com.amenity_reservation_system.entity.AmenityType;
import com.amenity_reservation_system.entity.Reservation;
import com.amenity_reservation_system.entity.User;
import com.amenity_reservation_system.mapper.AmenityTypeMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.ValidationException;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

@Service
@Transactional
public class AmenityTypeServiceImpl implements AmenityTypeService {

    private final AmenityTypeMapper MAPPER = AmenityTypeMapper.MAPPER;

    private final AmenityTypeRepository amenityTypeRepository;
    private final UserRepository userRepository;
    private final ReservationRepository reservationRepository;
    private final PhotoStorageCloudinaryService photoStorageCloudinaryService;
    private final String urlDefaultPhoto;

    public AmenityTypeServiceImpl(AmenityTypeRepository amenityTypeRepository, UserRepository userRepository, ReservationRepository reservationRepository, PhotoStorageCloudinaryService photoStorageCloudinaryService, String urlDefaultPhoto) {
        this.amenityTypeRepository = amenityTypeRepository;
        this.userRepository = userRepository;
        this.reservationRepository = reservationRepository;
        this.photoStorageCloudinaryService = photoStorageCloudinaryService;
        this.urlDefaultPhoto = urlDefaultPhoto;
    }

    @Override
    public List<AmenityTypeDTO> findAll() {
        return MAPPER.fromAmenityTypeList(amenityTypeRepository.findAll());
    }

    @Override
    public AmenityType findFirstByAmenityName(String amenityName) {
        return amenityTypeRepository.findFirstByAmenityName(amenityName);
    }


    @Override
    public void save(AmenityTypeDTO amenityTypeDTO, MultipartFile photo) throws IOException, ValidationException {
        String photoUrl;

        if (photo.getSize() == 0 && amenityTypeDTO.getId() == null) {
            photoUrl = urlDefaultPhoto;
        } else if (photo.getSize() == 0 && amenityTypeDTO.getId() != null) {
            photoUrl = amenityTypeDTO.getUrlPhoto();
        } else photoUrl = photoStorageCloudinaryService.getUrlPhoto(photo);


        if (amenityTypeDTO.getId() != null) { // Валидация названия при изменении
            if (checkUsername(amenityTypeDTO.getAmenityName(), amenityTypeDTO.getId())) {
                throw new ValidationException("A amenity with this name already exists");
            }
        } else if (checkUsername(amenityTypeDTO.getAmenityName())) // При создании
            throw new ValidationException("A amenity with this name already exists");

        if (amenityTypeDTO.getCapacity() <= 0)
            throw new ValidationException("The capacity can't be that small");

        if (amenityTypeDTO.getApiTemperature().isEmpty())
            amenityTypeDTO.setTemperature(null);

        amenityTypeRepository.save(AmenityType.builder()
                .id(amenityTypeDTO.getId())
                .amenityName(amenityTypeDTO.getAmenityName())
                .capacity(amenityTypeDTO.getCapacity())
                .urlPhoto(photoUrl)
                .apiTemperature(amenityTypeDTO.getApiTemperature())
                .build());
    }

    @Override
    public void deleteById(Long id) {
        List<Reservation> reservations = reservationRepository.getAllByAmenityType(amenityTypeRepository.getOne(id));
        reservations.forEach(reservation -> {
            User user = reservation.getUser();
            user.getReservations().remove(reservation);
            userRepository.save(user);
        });
        reservationRepository.deleteAll(reservations);
        amenityTypeRepository.deleteById(id);

    }

    @Override
    public AmenityTypeDTO getById(Long id) {
        return MAPPER.fromAmenityType(amenityTypeRepository.getOne(id));

    }

    @Override
    public boolean checkUsername(String amenityName) {
        AmenityType amenityType = amenityTypeRepository.findFirstByAmenityName(amenityName);
        return amenityType != null;
    }

    @Override
    public boolean checkUsername(String amenityName, Long id) {
        AmenityType amenityType = amenityTypeRepository.findFirstByAmenityName(amenityName);
        if (amenityType == null) return false;
        return !Objects.equals(id, amenityType.getId());
    }

}
