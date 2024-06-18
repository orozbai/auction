package org.example.registrationpage.services;

import org.example.registrationpage.dtos.UserRegisterDto;
import org.example.registrationpage.entities.UserEntity;
import org.example.registrationpage.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public void save(UserRegisterDto registerDto) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String encodedPassword = encoder.encode(registerDto.getPassword());
        userRepository.save(UserEntity.builder()
                .age(registerDto.getAge())
                .email(registerDto.getEmail())
                .name(registerDto.getName())
                .password(encodedPassword)
                .role("admin")
                .build());
    }

    public List<UserEntity> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<UserEntity> getUser(Long id) {
        return userRepository.findById(id);
    }

    public void deleteUserById(Long id) {
        userRepository.deleteById(id);
    }
}
