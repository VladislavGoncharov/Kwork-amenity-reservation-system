package com.amenity_reservation_system.controller.admin;

import com.amenity_reservation_system.dto.UserDTO;
import com.amenity_reservation_system.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.validation.ValidationException;

@Controller
@RequestMapping("/admin/users")
public class AdminUserController {

    final private UserService userService;

    public AdminUserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String adminUsersPage(Model model) {
        model.addAttribute("newUser", new UserDTO());
        model.addAttribute("allUsers", userService.findAll());
        return "admin-all-user";
    }

    @PostMapping("/save-user")
    public String adminSaveUser(@ModelAttribute("newUser") UserDTO userDTO, Model model) {
        try { // Валидация
            userService.save(userDTO);
        } catch (ValidationException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("newUser", userDTO);
            model.addAttribute("allUsers", userService.findAll());
            return "admin-all-user";
        }

        return "redirect:/admin/users";
    }

    @RequestMapping("/update-user-{id}")
    public String adminUpdateUser(@PathVariable Long id, Model model) {
        model.addAttribute("newUser", userService.getById(id));
        model.addAttribute("allUsers", userService.findAll());
        return "admin-all-user";
    }

    @RequestMapping("/delete-user-{id}")
    public String adminDeleteUser(@PathVariable Long id) {
        userService.deleteById(id);
        return "redirect:/admin/users";
    }

}
