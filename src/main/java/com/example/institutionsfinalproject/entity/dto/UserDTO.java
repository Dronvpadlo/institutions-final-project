package com.example.institutionsfinalproject.entity.dto;

import com.example.institutionsfinalproject.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private String id;
    private String name;
    private String surname;
    private String address;
    private Long age;
    private String email;
    private Role role;
    private List<String> favoriteInstitutionsIds;
    private List<String> myCommentsIds;
}
