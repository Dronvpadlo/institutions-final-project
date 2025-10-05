package com.example.institutionsfinalproject.entity.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRegistrationDTO {

    @NotBlank(message = "name is required")
    @Size(min = 2, max = 32, message = "Name must contained at least 2 and max 32 symbols")
    private String name;

    @NotBlank(message = "surname is required")
    @Size(min = 2, max = 32, message = "Surname must contained at least 2 and max 32 symbols")
    private String surname;

    @NotNull(message = "address is required")
    @Size(min = 6, max = 64, message = "Email address must be well-formed (e.g., example@domain.com)")
    private String address;

    @NotBlank(message = "age is required")
    @Min(value = 10, message = "age must be greater or equal 10")
    @Max(value = 117, message = "age must be less or equal 117")
    private Long age;

    @NotBlank(message = "email is required")
    @Email(message = "email must have @")
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 8, max = 32, message = "Password must contained at least 8 and max 32 symbols")
    private String password;
}
