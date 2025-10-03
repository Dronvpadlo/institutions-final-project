package com.example.institutionsfinalproject.service;

import com.example.institutionsfinalproject.entity.InstitutionEntity;
import com.example.institutionsfinalproject.entity.ModerationStatus;
import com.example.institutionsfinalproject.entity.ReviewEntity;
import com.example.institutionsfinalproject.entity.Statistics;
import com.example.institutionsfinalproject.entity.dto.InstitutionDTO;
import com.example.institutionsfinalproject.entity.dto.InstitutionFilterDTO;
import com.example.institutionsfinalproject.entity.dto.ResponseDTO;
import com.example.institutionsfinalproject.mapper.InstitutionMapper;
import com.example.institutionsfinalproject.repository.InstitutionRepository;
import com.example.institutionsfinalproject.repository.NewsRepository;
import com.example.institutionsfinalproject.repository.ReviewRepository;
import com.example.institutionsfinalproject.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class InstitutionService {

    private final InstitutionRepository institutionRepository;
    private final InstitutionMapper institutionMapper;
    private final NewsRepository newsRepository;
    private final ReviewRepository reviewRepository;

    private final MongoTemplate mongoTemplate;
    private final UserRepository userRepository;




    public InstitutionService(InstitutionRepository institutionRepository, InstitutionMapper institutionMapper, NewsRepository newsRepository, ReviewRepository reviewRepository, MongoTemplate mongoTemplate, UserRepository userRepository){
        this.institutionRepository = institutionRepository;
        this.institutionMapper = institutionMapper;
        this.newsRepository = newsRepository;
        this.reviewRepository = reviewRepository;
        this.mongoTemplate = mongoTemplate;
        this.userRepository = userRepository;
    }

    public void calculateAndSetRating(String institutionId){
        reviewRepository.findByInstitutionId(institutionId).ifPresent(reviews -> {
            if (!reviews.isEmpty()){
                double averageRating = reviews.stream()
                        .mapToInt(ReviewEntity::getRating)
                        .average()
                        .orElse(0.0);

                institutionRepository.findById(institutionId).ifPresent(institution -> {
                    institution.setRating(averageRating);
                    institutionRepository.save(institution);
                });
            } else {
                institutionRepository.findById(institutionId).ifPresent(institution -> {
                    institution.setRating(0.0);
                    institutionRepository.save(institution);
                });
            }

        });
    }

    public InstitutionDTO createInstitution(InstitutionDTO institutionDTO) {
        InstitutionEntity institutionEntity = institutionMapper.toEntity(institutionDTO);
        institutionEntity.setCreatedAt(Instant.now());
        institutionEntity.setRating(0.0);
        institutionEntity.setModerationStatus(ModerationStatus.PENDING);
        InstitutionEntity savedInstitution = institutionRepository.save(institutionEntity);
        return institutionMapper.toDto(savedInstitution);
    }

    public Optional<InstitutionDTO> getInstitutionById(String id){
        incrementInstitutionViewsAndClicks(id);
        return institutionRepository.findById(id)

                .map(institutionMapper::toDto);
    }


    public ResponseDTO<InstitutionDTO> getAllInstitutions(ModerationStatus status, int skip, int limit){
        Pageable pageable = PageRequest.of(skip / limit, limit);
        Page<InstitutionEntity> institutionsPage = institutionRepository.findAllByModerationStatus(status ,pageable);

        List<InstitutionDTO> institutions = institutionsPage.getContent()
                .stream()
                .map(institutionMapper::toDto)
                .collect(Collectors.toList());
        return new ResponseDTO<>(institutions, institutionsPage.getTotalElements(), skip, limit);
    }

    public ResponseDTO<InstitutionDTO> getInstitutionsByName(String name, int skip, int limit){
        Pageable pageable = PageRequest.of(skip/limit, limit);
        Page<InstitutionEntity> institutionsPage = institutionRepository.findByNameContainingIgnoreCase(name, pageable);
        List<InstitutionDTO> institution = institutionsPage.getContent()
                .stream()
                .map(institutionMapper::toDto)
                .collect(Collectors.toList());
        return new ResponseDTO<>(institution, institutionsPage.getTotalElements(), skip, limit);
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
                    existedInstitution.setName(institutionDTO.getName());
                    existedInstitution.setAverageCheck(institutionDTO.getAverageCheck());
                    existedInstitution.setPhotoUrls(institutionDTO.getPhotoUrls());
                    existedInstitution.setReviewsIds(institutionDTO.getReviewsIds());
                    existedInstitution.setTags(institutionDTO.getTags());
                    existedInstitution.setModerationStatus(ModerationStatus.PENDING);
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
                    institution.setModerationStatus(ModerationStatus.PENDING);
                    InstitutionEntity updatedInstitution = institutionRepository.save(institution);
                return institutionMapper.toDto(updatedInstitution);
        });
    }

    public Optional<InstitutionDTO> changeInstitutionModeratedStatus(String id, ModerationStatus newStatus){
        return institutionRepository.findById(id)
                .map(institution -> {
                    institution.setModerationStatus(newStatus);
                    institutionRepository.save(institution);
                    return institutionMapper.toDto(institution);
                });

    }

    public ResponseDTO<InstitutionDTO> getFilteredInstitution(InstitutionFilterDTO filterDTO, int skip, int limit){


        Query query = new Query();
        List<Criteria> criteriaList = new ArrayList<>();

        if (filterDTO.getMinRating() != null){
            criteriaList.add(Criteria.where("rating").gte(filterDTO.getMinRating()));
        }

        if (filterDTO.getType() != null){
            criteriaList.add(Criteria.where("type").is(filterDTO.getType()));
        }

        if (filterDTO.getMinAverageCheck() != null){
            criteriaList.add(Criteria.where("averageCheck").gte(filterDTO.getMinAverageCheck()));
        }
        if (filterDTO.getMaxAverageCheck() != null){
            criteriaList.add(Criteria.where("averageCheck").lte(filterDTO.getMaxAverageCheck()));
        }
        if (filterDTO.getTags() != null && !filterDTO.getTags().isEmpty()){
            criteriaList.add(Criteria.where("tags").in(filterDTO.getTags()));
        }
        if (filterDTO.getHasWifi() != null){
            criteriaList.add(Criteria.where("specification.hasWifi").in(filterDTO.getHasWifi()));
        }
        if (filterDTO.getHasParking() != null){
            criteriaList.add(Criteria.where("specification.hasParking").in(filterDTO.getHasParking()));
        }
        if (filterDTO.getHasLiveMusic() != null){
            criteriaList.add(Criteria.where("specification.hasLiveMusic").in(filterDTO.getHasLiveMusic()));
        }
        criteriaList.add(Criteria.where("moderationStatus").is(ModerationStatus.APPROVED));

        query.addCriteria(new Criteria().andOperator(criteriaList.toArray(new Criteria[0])));

        long total =mongoTemplate.count(query, InstitutionEntity.class);

        if (filterDTO.getSortBy() != null && filterDTO.getSortDirection() != null && filterDTO.getSortBy().size() == filterDTO.getSortDirection().size() && !filterDTO.getSortDirection().isEmpty()){
            List<Sort.Order> sortOrder = new ArrayList<>();

            List<String> validSortFields = Arrays.asList("rating", "averageCheck", "createdAt", "name");

            for (int i = 0; i < filterDTO.getSortBy().size(); i++) {
                String field = filterDTO.getSortBy().get(i);
                String directionString = filterDTO.getSortDirection().get(i);

                if(validSortFields.contains(field)){
                    Sort.Direction direction = directionString.equalsIgnoreCase("desc")
                            ? Sort.Direction.DESC
                            : Sort.Direction.ASC;

                    sortOrder.add(new Sort.Order(direction, field));
                }
            }

            if (!sortOrder.isEmpty()) {
                query.with(Sort.by(sortOrder));
            }
        }



        query.skip(skip).limit(limit);

        List<InstitutionEntity> filteredEntities = mongoTemplate.find(query, InstitutionEntity.class);

        List<InstitutionDTO> response = filteredEntities.stream()
                .map(institutionMapper::toDto)
                .collect(Collectors.toList());

        return new ResponseDTO<>(response, total, skip, limit);
    }

    public Optional<InstitutionDTO> assignNewOwner(String institutionId, String newOwnerId){
        if(!userRepository.existsById(newOwnerId)){
            return Optional.empty();
        }
        return institutionRepository.findById(institutionId)
                .map(institution -> {
                    institution.setOwnerId(newOwnerId);
                    institutionRepository.save(institution);
                    return institutionMapper.toDto(institution);
                });
    }

    public void incrementInstitutionViewsAndClicks(String institutionId){
        institutionRepository.findById(institutionId)
                .ifPresent(institution -> {
                    Statistics stats = institution.getStatistics();

                    if (stats == null){
                        stats = new Statistics();
                    }

                    stats.setViews(stats.getViews() + 1);
                    stats.setClicks(stats.getClicks() + 1);

                    institutionRepository.save(institution);
                });
    }

    private void updateLikesCount(String institutionId, long delta){
        institutionRepository.findById(institutionId)
                .ifPresent(institution -> {
                    Statistics stats = institution.getStatistics();

                    if (stats == null){
                        stats = new Statistics();
                        institution.setStatistics(stats);
                    }

                    long newLikes = Math.max(0, stats.getLikes() + delta);
                    stats.setLikes(newLikes);

                    institutionRepository.save(institution);
                });
    }

    public void incrementLikes(String institutionId){
        updateLikesCount(institutionId, 1);
    }

    public void decrementLikes(String institutionId){
        updateLikesCount(institutionId, -1);
    }
}
