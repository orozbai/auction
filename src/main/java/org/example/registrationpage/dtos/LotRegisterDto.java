package org.example.registrationpage.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LotRegisterDto {
    private Long id;
    private String name;
    private String description;
    private Integer quantity;
    private Integer price;
}
