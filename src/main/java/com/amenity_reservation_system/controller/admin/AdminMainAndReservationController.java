package com.amenity_reservation_system.controller.admin;

import com.amenity_reservation_system.dto.ChooseDateAndTime;
import com.amenity_reservation_system.dto.ReservationDTO;
import com.amenity_reservation_system.entity.Reservation;
import com.amenity_reservation_system.service.ReservationService;
import com.amenity_reservation_system.service.UserService;
import com.amenity_reservation_system.util.FreeTime;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/admin")
public class AdminMainAndReservationController {

    private final ReservationService reservationService;
    private final UserService userService;

    public AdminMainAndReservationController(ReservationService reservationService, UserService userService) {
        this.reservationService = reservationService;
        this.userService = userService;
    }

    @GetMapping()
    public String pageAdmin(Model model) {
        model.addAttribute("allReservations", reservationService.findAll());
        return "admin";
    }

    @RequestMapping("update-reservation-{id}")
    public String updateReservation(@PathVariable Long id, Model model, HttpSession httpSession) {
        Reservation reservation = reservationService.findById(id);

        model.addAttribute("dateAndTime", new ChooseDateAndTime());

        httpSession.setAttribute("freeTime",
                FreeTime.updateReservation(reservationService.getAllByAmenityType(reservation.getAmenityType()), reservation));
        httpSession.setAttribute("reservationDTO", new ReservationDTO(id, reservation.getAmenityType()));
        return "admin-update-reservation";

    }

    @RequestMapping("update-reservation-check-in-{userId}")
    public String updateReservationCheckIn(@PathVariable Long userId) {
        userService.updateCheckIn(userId);
        return "redirect:/admin";

    }

    @GetMapping("/update-error")
    public String updateAmenitiesError(Model model, HttpSession httpSession) {

        ReservationDTO reservationDTO = (ReservationDTO) httpSession.getAttribute("reservationDTO");
        model.addAttribute("error", "This time is busy, try again");

        return updateReservation(reservationDTO.getId(), model, httpSession);
    }

    @PostMapping("/update-success")
    public String updateSuccess(@ModelAttribute("dateAndTime") ChooseDateAndTime chooseDateAndTime, HttpSession httpSession) {
        chooseDateAndTime.checkTime();
        ReservationDTO reservationDTO = (ReservationDTO) httpSession.getAttribute("reservationDTO");

        if (FreeTime.checkingForAvailableSeats(httpSession.getAttribute("freeTime"), chooseDateAndTime)) {
            return "redirect:/admin/update-reservation-error";
        }

        reservationService.update(reservationDTO.getId(), chooseDateAndTime);
        return "redirect:/admin";

    }

    @RequestMapping("delete-reservation-{id}")
    public String deleteReservation(@PathVariable Long id) {
        reservationService.deleteById(id);
        return "redirect:/admin";
    }
}
