package com.example.institutionsfinalproject.controller;

import com.example.institutionsfinalproject.entity.dto.InstitutionDTO;
import com.example.institutionsfinalproject.service.InstitutionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/institution")
public class InstitutionController {

    private final InstitutionService institutionService;

    public InstitutionController(InstitutionService institutionService) {
        this.institutionService = institutionService;
    }

    @PostMapping
    public ResponseEntity<InstitutionDTO> createInstitution(@RequestBody InstitutionDTO institutionDTO){
        InstitutionDTO newInstitution = institutionService.createInstitution(institutionDTO);
        return new ResponseEntity<>(newInstitution, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<InstitutionDTO>> getAllInstitution(){
        List<InstitutionDTO> institutions = institutionService.getAllInstitution();
        return new ResponseEntity<>(institutions, HttpStatus.OK);
    }
}
