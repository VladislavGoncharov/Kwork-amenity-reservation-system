package com.amenity_reservation_system.controller;

import com.amenity_reservation_system.dto.ChooseDateAndTime;
import com.amenity_reservation_system.dto.ReservationDTO;
import com.amenity_reservation_system.dto.UserDTO;
import com.amenity_reservation_system.entity.BookingTimeEnum;
import com.amenity_reservation_system.entity.Reservation;
import com.amenity_reservation_system.entity.User;
import com.amenity_reservation_system.service.AmenityTypeService;
import com.amenity_reservation_system.service.ReservationService;
import com.amenity_reservation_system.service.UserService;
import com.amenity_reservation_system.util.FreeTime;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.List;

@Controller
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

    @GetMapping("/")
    public String mainPage(Model model, Principal principal) {
        model.addAttribute("user", new UserDTO(userService.findFirstByUsername(principal.getName())));
        model.addAttribute("allAmenityType", amenityTypeService.findAll());
        return "main";

    }

    @GetMapping("/book-{amenityName}")
    public String bookAmenities(@PathVariable String amenityName, Model model, Principal principal,
                                HttpServletRequest httpServletRequest) {
        User user = userService.findFirstByUsername(principal.getName());

        List<List<BookingTimeEnum>> freeTime = FreeTime.sortByDate(reservationService.findAll(),
                amenityTypeService.findFirstByAmenityName(amenityName));

        model.addAttribute("weekList", freeTime);
        model.addAttribute("userFullName", user.getFullName());
        model.addAttribute("dateAndTime", new ChooseDateAndTime());

        httpServletRequest.getSession().setAttribute("freeTime", freeTime);
        httpServletRequest.getSession().setAttribute("reservationDTO",
                new ReservationDTO(user, amenityTypeService.findFirstByAmenityName(amenityName)));
        return "choose-time";

    }

    @GetMapping("/book-{amenityName}-error")
    public String bookAmenitiesError(@PathVariable String amenityName, Model model, Principal principal,
                                     HttpServletRequest httpServletRequest) {

        ReservationDTO reservationDTO = (ReservationDTO) httpServletRequest.getSession().getAttribute("reservationDTO");
        model.addAttribute("error", "This time is busy, try again");

        if (reservationDTO.getId() != null) return updateReservation(reservationDTO.getId(), model, httpServletRequest);
        else return bookAmenities(amenityName, model, principal, httpServletRequest);
    }


    @PostMapping("/reserve-time")
    public String reserveTime(@ModelAttribute("dateAndTime") ChooseDateAndTime chooseDateAndTime,
                              HttpServletRequest httpServletRequest) {
        chooseDateAndTime.checkTime();
        ReservationDTO reservationDTO = (ReservationDTO) httpServletRequest.getSession().getAttribute("reservationDTO");

        if (FreeTime.checkingForAvailableSeats(httpServletRequest.getSession().getAttribute("freeTime"), chooseDateAndTime)) {
            return "redirect:/book-" + reservationDTO.getAmenityType().getAmenityName() + "-error";
        }

        if (reservationDTO.getId() != null) reservationService.update(reservationDTO.getId(), chooseDateAndTime);
        else reservationService.save(reservationDTO, chooseDateAndTime);

        return "redirect:/";
    }

    @RequestMapping("/delete-{id}")
    public String deleteReservation(@PathVariable Long id) {
        reservationService.deleteById(id);
        return "redirect:/";
    }

    @RequestMapping("/update-{id}")
    public String updateReservation(@PathVariable Long id, Model model, HttpServletRequest httpServletRequest) {
        Reservation reservation = reservationService.findById(id);

        List<List<BookingTimeEnum>> freeTime =
                FreeTime.updateReservation(reservationService.findAll(), reservation.getAmenityType(), id);

        model.addAttribute("weekList", freeTime);
        model.addAttribute("userFullName", reservation.getUser().getFullName());
        model.addAttribute("dateAndTime", new ChooseDateAndTime());

        httpServletRequest.getSession().setAttribute("freeTime", freeTime);
        httpServletRequest.getSession().setAttribute("reservationDTO",
                new ReservationDTO(reservation.getId(), reservation.getAmenityType()));
        return "choose-time";
    }
}
