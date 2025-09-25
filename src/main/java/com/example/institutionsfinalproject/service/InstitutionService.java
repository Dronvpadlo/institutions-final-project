package com.example.institutionsfinalproject.service;

import com.example.institutionsfinalproject.entity.InstitutionEntity;
import com.example.institutionsfinalproject.entity.dto.InstitutionDTO;
import com.example.institutionsfinalproject.entity.dto.ResponseDTO;
import com.example.institutionsfinalproject.mapper.InstitutionMapper;
import com.example.institutionsfinalproject.repository.InstitutionRepository;
import com.example.institutionsfinalproject.repository.NewsRepository;
import com.example.institutionsfinalproject.repository.ReviewRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class InstitutionService {

    private final InstitutionRepository institutionRepository;
    private final InstitutionMapper institutionMapper;
    private final NewsRepository newsRepository;
    private final ReviewRepository reviewRepository;


    public InstitutionService(InstitutionRepository institutionRepository, InstitutionMapper institutionMapper, NewsRepository newsRepository, ReviewRepository reviewRepository){
        this.institutionRepository = institutionRepository;
        this.institutionMapper = institutionMapper;
        this.newsRepository = newsRepository;
        this.reviewRepository = reviewRepository;
    }

    public InstitutionDTO createInstitution(InstitutionDTO institutionDTO) {
        InstitutionEntity institutionEntity = institutionMapper.toEntity(institutionDTO);
        InstitutionEntity savedInstitution = institutionRepository.save(institutionEntity);
        return institutionMapper.toDto(savedInstitution);
    }

    public Optional<InstitutionDTO> getInstitutionById(String id){
        return institutionRepository.findById(id)
                .map(institutionMapper::toDto);
    }


    public ResponseDTO<InstitutionDTO> getAllInstitutions(int skip, int limit){
        Pageable pageable = PageRequest.of(skip / limit, limit);
        Page<InstitutionEntity> institutionsPage = institutionRepository.findAll(pageable);

        List<InstitutionDTO> institutions = institutionsPage.getContent()
                .stream()
                .map(institutionMapper::toDto)
                .collect(Collectors.toList());
        return new ResponseDTO<>(institutions, institutionsPage.getTotalElements(), skip, limit);
    }

    public void  deleteInstitutionById(String id){
        institutionRepository.findById(id).ifPresent(institution -> {
            List<String> newsIds = institution.getNewsIds();
            List<String> reviewIds = institution.getReviewsIds();
            if (newsIds != null && !newsIds.isEmpty()){
                newsRepository.deleteAllById(newsIds);
            }
            if (reviewIds != null && !reviewIds.isEmpty()){
                reviewRepository.deleteAllById(reviewIds);
            }
            institutionRepository.deleteById(id);
        });
    }

    public Optional<InstitutionDTO> updateInstitution(String id, InstitutionDTO institutionDTO){
        return institutionRepository.findById(id)
                .map(existedInstitution -> {
                    existedInstitution.setNewsIds(institutionDTO.getNewsIds());
                    existedInstitution.setLocation(institutionDTO.getLocation());
                    existedInstitution.setContacts(institutionDTO.getContacts());
                    existedInstitution.setRating(institutionDTO.getRating());
                    existedInstitution.setName(institutionDTO.getName());
                    existedInstitution.setAverageCheck(institutionDTO.getAverageCheck());
                    existedInstitution.setPhotoUrls(institutionDTO.getPhotoUrls());
                    existedInstitution.setReviewsIds(institutionDTO.getReviewsIds());
                    existedInstitution.setTags(institutionDTO.getTags());
                    existedInstitution.setOpenAt(LocalTime.parse(institutionDTO.getOpenAt()));
                    existedInstitution.setCloseAt(LocalTime.parse(institutionDTO.getCloseAt()));
                    institutionRepository.save(existedInstitution);
                    return institutionMapper.toDto(existedInstitution);
                });
    }

    public Optional<InstitutionDTO> patchInstitution(String id, Map<String, Object> updates){
        return institutionRepository.findById(id)
                .map(institution -> {
                    updates.forEach((key, value) ->{
                        switch (key){
                            case "name": institution.setName((String) value); break;
                            case "location": institution.setLocation((String) value); break;
                            case "contacts": institution.setContacts((String) value); break;
                            case "averageCheck":
                                if (value instanceof Number) {
                                    institution.setAverageCheck((double) value);
                                    break;
                                }
                            case "rating":
                                if (value instanceof Number) {
                                    institution.setRating((double) value);
                                    break;
                                }
                            case "openAt":
                                try{
                                    institution.setOpenAt(LocalTime.parse((String) value));break;
                                } catch (Exception e){
                                    throw new IllegalArgumentException("Invalid Local Time value: " + value);
                                }

                            case "closeAt":
                                try{
                                institution.setCloseAt(LocalTime.parse((String) value));break;
                                } catch (Exception e){
                                    throw new IllegalArgumentException("Invalid Local Time value: " + value);
                                }
                            }
                    });
                    InstitutionEntity updatedInstitution = institutionRepository.save(institution);
                return institutionMapper.toDto(updatedInstitution);
        });
    }
}
