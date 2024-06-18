package org.example.registrationpage.controllers;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.example.registrationpage.dtos.UserRegisterDto;
import org.example.registrationpage.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@RestController
@AllArgsConstructor
public class RegistrationController {
    @Autowired
    private UserService userService;

    @GetMapping("/register")
    public ModelAndView getRegister() {
        return new ModelAndView("register");
    }

    @PostMapping("/register")
    public ModelAndView registerUser(@RequestBody UserRegisterDto registerDto) {
        userService.save(registerDto);
        return new ModelAndView("registrationDone");
    }

    @GetMapping("/login")
    public ModelAndView loginUser() {
        return new ModelAndView("login");
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        SecurityContextLogoutHandler logoutHandler = new SecurityContextLogoutHandler();
        logoutHandler.logout(request, response, SecurityContextHolder.getContext().getAuthentication());
        return "redirect:/login";
    }

    @GetMapping("/welcome")
    public ModelAndView welcome() {
        return new ModelAndView("welcome");
    }

    @GetMapping("/users")
    public ModelAndView pageForUsers() {
        return new ModelAndView("users");
    }

    @GetMapping("/admins")
    public ModelAndView pageForAdmins() {
        return new ModelAndView("admins");
    }

}
