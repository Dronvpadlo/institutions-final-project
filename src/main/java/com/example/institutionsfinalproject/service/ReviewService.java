package com.example.institutionsfinalproject.service;

import com.example.institutionsfinalproject.entity.ReviewEntity;
import com.example.institutionsfinalproject.entity.dto.ResponseDTO;
import com.example.institutionsfinalproject.entity.dto.ReviewDTO;
import com.example.institutionsfinalproject.mapper.ReviewMapper;
import com.example.institutionsfinalproject.repository.InstitutionRepository;
import com.example.institutionsfinalproject.repository.ReviewRepository;
import com.example.institutionsfinalproject.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final ReviewMapper reviewMapper;
    private final InstitutionRepository institutionRepository;
    private final UserRepository userRepository;


    public ReviewService(ReviewRepository reviewRepository, ReviewMapper reviewMapper, InstitutionRepository institutionRepository, UserRepository userRepository){
        this.reviewMapper = reviewMapper;
        this.reviewRepository = reviewRepository;
        this.institutionRepository = institutionRepository;
        this.userRepository = userRepository;
    }

    private void updatedRelatedEntities(String reviewId, String institutionId, String customerId){
        institutionRepository.findById(institutionId).ifPresent(institution -> {
            List<String> reviewsIds = institution.getReviewsIds();
            if (reviewsIds == null){
                reviewsIds = new ArrayList<>();
            }
            reviewsIds.add(reviewId);
            institution.setReviewsIds(reviewsIds);
            institutionRepository.save(institution);
        });

        userRepository.findById(customerId).ifPresent(user -> {
            List<String> myCommentsIds = user.getMyCommentsIds();
            if (myCommentsIds == null){
                myCommentsIds = new ArrayList<>();
            }
            myCommentsIds.add(reviewId);
            user.setMyCommentsIds(myCommentsIds);
            userRepository.save(user);
        });
    }

    private void removeReviewFromRelatedEntities(String reviewId, String institutionId, String customerId){
        institutionRepository.findById(institutionId).ifPresent(institution -> {
            List<String> reviewsIds = institution.getReviewsIds();
            if (reviewsIds != null){
                reviewsIds.remove(reviewId);
                institution.setReviewsIds(reviewsIds);
                institutionRepository.save(institution);
            }
        });

        userRepository.findById(customerId).ifPresent(user -> {
            List<String> myCommentsIds = user.getMyCommentsIds();
            if (myCommentsIds != null){
                myCommentsIds.remove(reviewId);
                user.setMyCommentsIds(myCommentsIds);
                userRepository.save(user);
            }
        });
    }

    public ReviewDTO createReview (ReviewDTO reviewDTO){
        ReviewEntity reviewEntity = reviewMapper.toEntity(reviewDTO);
        ReviewEntity savedReview = reviewRepository.save(reviewEntity);
        updatedRelatedEntities(savedReview.getId(), reviewDTO.getInstitutionId(), reviewDTO.getCustomerId());

        return reviewMapper.toDto(savedReview);
    }

    public ResponseDTO<ReviewDTO> getAllReviews(int skip, int limit){
        Pageable pageable = PageRequest.of(skip/limit, limit);
        Page<ReviewEntity> reviewPage = reviewRepository.findAll(pageable);

        List<ReviewDTO> reviews = reviewPage.getContent()
                .stream().map(reviewMapper::toDto)
                .collect(Collectors.toList());
        return new ResponseDTO<>(reviews, reviewPage.getTotalElements(), skip, limit);
    }

    public Optional<ReviewDTO> getReviewById(String id){
        return reviewRepository.findById(id)
                .map(reviewMapper::toDto);
    }

    public void deleteReview(String id){
        reviewRepository.findById(id).ifPresent(review -> {
            removeReviewFromRelatedEntities(review.getId(), review.getInstitutionId(), review.getCustomerId());
            reviewRepository.deleteById(id);
        });
    }

    public Optional<ReviewDTO> updateReview(String id, ReviewDTO reviewDTO){
        return reviewRepository.findById(id)
                .map(existedReview -> {
                    ReviewEntity updatedReview = reviewMapper.toEntity(reviewDTO);
                    updatedReview.setId(existedReview.getId());

                    reviewRepository.save(updatedReview);

                    return reviewMapper.toDto(updatedReview);
                });
    }

    public Optional<ReviewDTO> patchReview(String id, Map<String, Object> updates){
        return reviewRepository.findById(id)
                .map(review -> {
                    updates.forEach((key, value) ->{
                        switch (key){
                            case "description": review.setDescription((String) value); break;
                            case "rating":
                                try {
                                    review.setRating(Integer.parseInt(value.toString()));
                                } catch (NumberFormatException e) {
                                    throw new IllegalArgumentException("Invalid value for 'rating': " + value);
                                }
                                break;
                            case "customerId": review.setCustomerId((String) value); break;
                            case "institutionId": review.setInstitutionId((String) value); break;
                            case "checkAmount":
                                try {
                                    review.setCheckAmount(Double.parseDouble(value.toString()));
                                } catch (NumberFormatException e) {
                                    throw new IllegalArgumentException("Invalid value for 'checkAmount': " + value);
                                }
                                break;
                        }
                    });
                    ReviewEntity updatedReview = reviewRepository.save(review);
                    return reviewMapper.toDto(updatedReview);
                });
    }
}
