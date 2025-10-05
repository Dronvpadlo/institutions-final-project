package com.example.institutionsfinalproject.entity.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class SpecificationsDTO {

    @NotNull(message = "Wifi field is required")
    private Boolean hasWifi;

    @NotNull(message = "Parking field is required")
    private Boolean hasParking;

    @NotNull(message = "Live Music field is required")
    private Boolean hasLiveMusic;
}
