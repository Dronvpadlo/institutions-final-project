package com.example.institutionsfinalproject.entity.dto;

import lombok.Builder;
import lombok.Data;

import java.util.Map;

@Data
@Builder
public class ValidatorErrorDTO {

    private int status;

    private String message;

    private Map<String, String> errors;
}
