package org.example.registrationpage;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class RegistrationPageApplication {

    public static void main(String[] args) {
        SpringApplication.run(RegistrationPageApplication.class, args);
        System.out.println("http://localhost:8080/login");
    }

}
