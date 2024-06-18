package org.example.registrationpage.controllers;

import lombok.AllArgsConstructor;
import org.example.registrationpage.entities.UserEntity;
import org.example.registrationpage.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@AllArgsConstructor
public class GetUsersController {
    @Autowired
    private UserService userService;

    @GetMapping("get-users")
    private ResponseEntity<?> getAllUsers(Model model) {
        List<UserEntity> users = userService.getAllUsers();
        model.addAttribute("users", users);
        if (users != null) {
            return new ResponseEntity<>("User registered successfully", HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>("User registration failed", HttpStatus.BAD_REQUEST);
        }
    }
    @GetMapping("get-user-by-id")
    @ResponseBody
    private Optional<UserEntity> getUser(@RequestParam Long id, Model model) {
        Optional<UserEntity> user = userService.getUser(id);
        model.addAttribute("user", user);
        if (user.isPresent()) {
            return userService.getUser(id);
        }
        return Optional.empty();
    }

    @PostMapping("delete-user-by-id")
    @ResponseBody
    private void deleteUser (@RequestParam Long id) {
        userService.deleteUserById(id);
    }
}
