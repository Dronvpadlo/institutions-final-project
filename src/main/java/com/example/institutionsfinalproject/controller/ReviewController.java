package com.example.institutionsfinalproject.controller;

import com.example.institutionsfinalproject.entity.dto.ReviewDTO;
import com.example.institutionsfinalproject.service.ReviewService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/review")
public class ReviewController {

    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @GetMapping
    public ResponseEntity<List<ReviewDTO>> getReviews(){
        List<ReviewDTO> reviews = reviewService.getAllReviews();
        return new ResponseEntity<>(reviews, HttpStatus.OK);
    }

    @PostMapping
    private ResponseEntity<ReviewDTO> postReview(@RequestBody ReviewDTO reviewDTO){
        ReviewDTO newReview = reviewService.createReview(reviewDTO);
        return new ResponseEntity<>(newReview, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    private ResponseEntity<String> deleteReview(@PathVariable String id){
        reviewService.deleteReview(id);
        return new ResponseEntity<>("Review was deleted", HttpStatus.NO_CONTENT);
    }
}
