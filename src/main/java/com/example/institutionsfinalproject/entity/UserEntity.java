package com.example.institutionsfinalproject.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;

@Document(collection = "users")
public class UserEntity {
    @Id
    private String id;
    private String name;
    private String surname;
    private String address;
    private Long age;
    private String email;
    @Field("role")
    private Role role;
    private List<String> favoriteInstitutionsIds;
    private List<String> myCommentsIds;
}
