package com.amenity_reservation_system.controller.admin;

import com.amenity_reservation_system.dto.ChooseDateAndTime;
import com.amenity_reservation_system.dto.ReservationDTO;
import com.amenity_reservation_system.entity.BookingTimeEnum;
import com.amenity_reservation_system.entity.Reservation;
import com.amenity_reservation_system.service.ReservationService;
import com.amenity_reservation_system.util.FreeTime;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminMainAndReservationController {

    private final ReservationService reservationService;

    public AdminMainAndReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @GetMapping()
    public String pageAdmin(Model model) {
        model.addAttribute("allReservations", reservationService.findAll());
        return "admin";
    }

    @RequestMapping("update-reservation-{id}")
    public String updateReservation(@PathVariable Long id, Model model, HttpServletRequest httpServletRequest) {
        Reservation reservation = reservationService.findById(id);

        List<List<BookingTimeEnum>> freeTime =
                FreeTime.updateReservation(reservationService.findAll(),
                        reservation.getAmenityType(),
                        id);

        model.addAttribute("weekList", freeTime);
        model.addAttribute("userFullName", reservation.getUser().getFullName());
        model.addAttribute("dateAndTime", new ChooseDateAndTime());

        httpServletRequest.getSession().setAttribute("freeTime", freeTime);
        httpServletRequest.getSession().setAttribute("reservationDTO", new ReservationDTO(id, reservation.getAmenityType()));
        return "admin-update-reservation";

    }

    @GetMapping("/update-error")
    public String updateAmenitiesError(Model model, HttpServletRequest httpServletRequest) {

        ReservationDTO reservationDTO = (ReservationDTO) httpServletRequest.getSession().getAttribute("reservationDTO");
        model.addAttribute("error", "This time is busy, try again");

        return updateReservation(reservationDTO.getId(), model, httpServletRequest);
    }

    @PostMapping("/update-success")
    public String updateSuccess(@ModelAttribute("dateAndTime") ChooseDateAndTime chooseDateAndTime, HttpServletRequest httpServletRequest) {
        chooseDateAndTime.checkTime();
        ReservationDTO reservationDTO = (ReservationDTO) httpServletRequest.getSession().getAttribute("reservationDTO");

        if (FreeTime.checkingForAvailableSeats(httpServletRequest.getSession().getAttribute("freeTime"), chooseDateAndTime)) {
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
