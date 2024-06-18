package org.example.registrationpage.entities;


import lombok.*;

//@Entity
//@Table(name = "users")
@Builder(toBuilder = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserEntity {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private Integer age;
    private String password;
    private String email;
    private String role;

    @Override
    public String toString() {
        return "UserEntity{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", age=" + age +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", role='" + role + '\'' +
                '}';
    }
}
