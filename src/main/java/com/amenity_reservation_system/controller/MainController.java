package com.amenity_reservation_system.controller;

import com.amenity_reservation_system.dto.ChooseDateAndTime;
import com.amenity_reservation_system.dto.ReservationDTO;
import com.amenity_reservation_system.dto.UserDTO;
import com.amenity_reservation_system.entity.AmenityType;
import com.amenity_reservation_system.entity.Reservation;
import com.amenity_reservation_system.mapper.UserMapper;
import com.amenity_reservation_system.service.AmenityTypeService;
import com.amenity_reservation_system.service.ReservationService;
import com.amenity_reservation_system.service.UserService;
import com.amenity_reservation_system.util.FreeTime;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.security.Principal;

@Controller
@RequestMapping("/main")
public class MainController {

    private final UserService userService;
    private final AmenityTypeService amenityTypeService;
    private final ReservationService reservationService;

    public MainController(UserService userService, AmenityTypeService amenityTypeService,
                          ReservationService reservationService) {
        this.userService = userService;
        this.amenityTypeService = amenityTypeService;
        this.reservationService = reservationService;
    }

    @GetMapping
    public String mainPage(Model model, Principal principal) {
        model.addAttribute("user", userService.findFirstByUsername(principal.getName()));
        model.addAttribute("allAmenityType", amenityTypeService.findAll());
        return "main";

    }

    @GetMapping("/book-{amenityName}")
    public String bookAmenities(@PathVariable String amenityName, Model model, Principal principal, HttpSession httpSession) {
        UserDTO userDTO = userService.findFirstByUsername(principal.getName());
        AmenityType amenityType = amenityTypeService.findFirstByAmenityName(amenityName);

        model.addAttribute("dateAndTime", new ChooseDateAndTime());

        httpSession.setAttribute("freeTime",
                FreeTime.sortByDate(reservationService.findAll(), amenityType.getCapacity()));
        httpSession.setAttribute("reservationDTO",
                new ReservationDTO(userDTO.getUsername(), amenityType));
        return "choose-time";

    }

    @GetMapping("/book-{amenityName}-error")
    public String bookAmenitiesError(@PathVariable String amenityName, Model model, Principal principal
            , HttpSession httpSession) {

        ReservationDTO reservationDTO = (ReservationDTO) httpSession.getAttribute("reservationDTO");
        model.addAttribute("error", "This time is busy, try again");

        if (reservationDTO.getId() != null) return updateReservation(reservationDTO.getId(), model, httpSession);
        else return bookAmenities(amenityName, model, principal, httpSession);
    }


    @PostMapping("/reserve-time")
    public String reserveTime(@ModelAttribute("dateAndTime") ChooseDateAndTime chooseDateAndTime, HttpSession httpSession) {
        chooseDateAndTime.checkTime();
        ReservationDTO reservationDTO = (ReservationDTO) httpSession.getAttribute("reservationDTO");

        if (FreeTime.checkingForAvailableSeats(httpSession.getAttribute("freeTime"), chooseDateAndTime))
            return "redirect:/main/book-" + reservationDTO.getAmenityType().getAmenityName() + "-error";

        if (reservationDTO.getId() != null) reservationService.update(reservationDTO.getId(), chooseDateAndTime);
        else reservationService.save(reservationDTO, chooseDateAndTime);

        return "redirect:/main";
    }

    @RequestMapping("/delete-{id}")
    public String deleteReservation(@PathVariable Long id) {
        reservationService.deleteById(id);
        return "redirect:/main";
    }

    @RequestMapping("/update-{id}")
    public String updateReservation(@PathVariable Long id, Model model, HttpSession httpSession) {
        Reservation reservation = reservationService.findById(id);

        model.addAttribute("dateAndTime", new ChooseDateAndTime());
        httpSession.setAttribute("freeTime",
                FreeTime.updateReservation(reservationService.getAllByAmenityType(reservation.getAmenityType()), reservation));
        httpSession.setAttribute("reservationDTO",
                new ReservationDTO(reservation.getId(), reservation.getAmenityType()));
        return "choose-time";
    }
}
