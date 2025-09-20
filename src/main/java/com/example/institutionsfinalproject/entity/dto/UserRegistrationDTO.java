package com.example.institutionsfinalproject.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "users")
public class UserRegistrationDTO {

    private String name;
    private String surname;
    private String address;
    private Long age;
    private String email;
    private String password;
}
