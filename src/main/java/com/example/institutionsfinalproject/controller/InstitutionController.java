package com.example.institutionsfinalproject.controller;

import com.example.institutionsfinalproject.entity.dto.InstitutionDTO;
import com.example.institutionsfinalproject.service.InstitutionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

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
        List<InstitutionDTO> institutions = institutionService.getAllInstitutions();
        return new ResponseEntity<>(institutions, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<InstitutionDTO> getInstitutionById(@PathVariable String id){
        return institutionService.getInstitutionById(id)
                .map(institutionDTO -> new ResponseEntity<>(institutionDTO, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteInstitutionById(@PathVariable String id){
        institutionService.deleteInstitutionById(id);
        return new ResponseEntity<>("Institution was deleted successful", HttpStatus.NO_CONTENT);
    }

    @PutMapping("/{id}")
    public ResponseEntity<InstitutionDTO> putInstitutionById(@PathVariable String id, @RequestBody InstitutionDTO institutionDTO){
        return institutionService.updateInstitution(id, institutionDTO)
                .map(updatedInstitution -> new ResponseEntity<>(updatedInstitution, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<InstitutionDTO> patchInstitutionById(@PathVariable String id, @RequestBody Map<String, Object> updates){
        return institutionService.patchInstitution(id, updates)
                .map(updatesInstitution -> new ResponseEntity<>(updatesInstitution, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
}
