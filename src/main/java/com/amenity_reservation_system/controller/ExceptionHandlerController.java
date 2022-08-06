package com.amenity_reservation_system.controller;

import com.amenity_reservation_system.dto.AmenityTypeDTO;
import com.amenity_reservation_system.service.AmenityTypeService;
import org.apache.tomcat.util.http.fileupload.impl.FileSizeLimitExceededException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Controller
@ControllerAdvice
public class ExceptionHandlerController {

    private final AmenityTypeService amenityTypeService;

    public ExceptionHandlerController(AmenityTypeService amenityTypeService) {
        this.amenityTypeService = amenityTypeService;
    }

    @ExceptionHandler(FileSizeLimitExceededException.class)
    public String handleFileSizeLimitExceeded(Model model) {
        model.addAttribute("error", "Photo size limit exceeded, max 2 MB");
        model.addAttribute("newAmenityType", new AmenityTypeDTO());
        model.addAttribute("allAmenityType", amenityTypeService.findAll());
        return "admin-all-amenity";
    }

}
