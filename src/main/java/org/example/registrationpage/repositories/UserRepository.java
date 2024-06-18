package org.example.registrationpage.repositories;

import org.example.registrationpage.dtos.UserRegisterDto;
import org.example.registrationpage.entities.UserEntity;


import java.util.List;
import java.util.Optional;

public interface UserRepository {
    UserEntity getUserById(Long id);
    void saveUser(UserRegisterDto user);
    void deleteUser(Long id);
    List<UserEntity> getAllUsers();

    Optional<UserEntity> findByName(String username);

    void updateUserById(UserRegisterDto updatedUser);
}

