package org.example.registrationpage.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.registrationpage.entities.UserEntity;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class UserDto {
    public static UserDto from(UserEntity userEntity) {
        return builder()
                .id(userEntity.getId())
                .name(userEntity.getName())
                .age(userEntity.getAge())
                .password(userEntity.getPassword())
                .email(userEntity.getEmail())
                .build();
    }

    private Long id;
    private String name;
    private Integer age;
    private String password;
    private String email;
}
