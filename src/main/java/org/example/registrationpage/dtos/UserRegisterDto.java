package org.example.registrationpage.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserRegisterDto {
    private String name;
    private Integer age;
    private String password;
    private String email;
}
