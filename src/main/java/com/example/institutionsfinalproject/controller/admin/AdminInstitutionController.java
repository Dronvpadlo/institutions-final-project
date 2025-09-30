package com.example.institutionsfinalproject.controller.admin;

import com.example.institutionsfinalproject.entity.ModerationStatus;
import com.example.institutionsfinalproject.entity.dto.AssignOwnerRequestDTO;
import com.example.institutionsfinalproject.entity.dto.InstitutionDTO;
import com.example.institutionsfinalproject.entity.dto.ResponseDTO;
import com.example.institutionsfinalproject.service.InstitutionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
public class AdminInstitutionController {

    private final InstitutionService institutionService;

    public AdminInstitutionController(InstitutionService institutionService) {
        this.institutionService = institutionService;
    }
    @GetMapping("/institution")
    public ResponseEntity<ResponseDTO<InstitutionDTO>> getAllInstitution(@RequestParam(defaultValue = "0")int skip, @RequestParam(defaultValue = "10") int limit){
        ResponseDTO<InstitutionDTO> institutions = institutionService.getAllInstitutions(ModerationStatus.PENDING, skip, limit);
        return new ResponseEntity<>(institutions, HttpStatus.OK);
    }

    @PatchMapping("/institution/{id}/status")
    public ResponseEntity<InstitutionDTO> updateInstitutionStatus(@PathVariable String id, @RequestParam ModerationStatus status){
        return institutionService.changeInstitutionModeratedStatus(id, status)
                .map(institutionDTO -> new ResponseEntity<>(institutionDTO, HttpStatus.OK))
                .orElseGet(()-> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PatchMapping("/institution/{institutionId}/owner")
    public ResponseEntity<InstitutionDTO> assignOwner(
            @PathVariable String institutionId,
            @RequestBody AssignOwnerRequestDTO requestDTO
    ){
        return institutionService.assignNewOwner(institutionId, requestDTO.getNewOwnerId())
                .map(institutionDTO -> new ResponseEntity<>(institutionDTO, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
}
