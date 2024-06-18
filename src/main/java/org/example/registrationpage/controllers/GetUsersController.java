package org.example.registrationpage.controllers;

import lombok.AllArgsConstructor;
import org.example.registrationpage.dtos.UserRegisterDto;
import org.example.registrationpage.entities.UserEntity;
import org.example.registrationpage.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping("/auc/get-user-by-id/{userid}")
    private UserEntity getUser(@PathVariable Long userid, Model model) {
        UserEntity user = userService.getUser(userid);
        model.addAttribute("user", user);
        return user;
    }

    @DeleteMapping("/auc/delete-user-by-id/{userid}")
    private void deleteUser(@PathVariable Long userid) {
        userService.deleteUserById(userid);
    }

    @PostMapping("/auc/update-user")
    private void updateUser(@RequestBody UserRegisterDto updatedUser) {
        userService.updateUser(updatedUser);
    }

}
