package com.amenity_reservation_system.controller.admin;

import com.amenity_reservation_system.dto.AmenityTypeDTO;
import com.amenity_reservation_system.service.AmenityTypeService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.annotation.MultipartConfig;
import javax.validation.ValidationException;
import java.io.IOException;

@Controller
@RequestMapping("/admin/amenities")
@MultipartConfig
public class AdminAmenityController {

    final private AmenityTypeService amenityTypeService;

    public AdminAmenityController(AmenityTypeService amenityTypeService) {
        this.amenityTypeService = amenityTypeService;
    }

    @GetMapping
    public String adminAmenityTypePage(Model model) {
        model.addAttribute("newAmenityType", new AmenityTypeDTO());
        model.addAttribute("allAmenityType", amenityTypeService.findAll());
        return "admin-all-amenity";
    }


    @PostMapping("/save-amenity")
    public String adminSaveAmenityType(@ModelAttribute AmenityTypeDTO amenityTypeDTO, Model model,
                                       @RequestParam("photoAmenity") MultipartFile photo) {
        try { // валидация
            amenityTypeService.save(amenityTypeDTO, photo);
        } catch (IOException | ValidationException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("newAmenityType", amenityTypeDTO);
            model.addAttribute("allAmenityType", amenityTypeService.findAll());
            return "admin-all-amenity";
        }

        return "redirect:/admin/amenities";
    }

    @RequestMapping("/update-amenity-{id}")
    public String adminUpdateAmenity(@PathVariable Long id, Model model) {
        model.addAttribute("newAmenityType", amenityTypeService.getById(id));
        model.addAttribute("allAmenityType", amenityTypeService.findAll());
        return "admin-all-amenity";
    }

    @RequestMapping("/delete-amenity-{id}")
    public String adminDeleteAmenity(@PathVariable Long id) {
        amenityTypeService.deleteById(id);
        return "redirect:/admin/amenities";
    }

}
