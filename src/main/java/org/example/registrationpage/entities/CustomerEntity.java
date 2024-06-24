package org.example.registrationpage.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerEntity {
    private Long id;
    private Long lotId;
    private Long userId;
}
