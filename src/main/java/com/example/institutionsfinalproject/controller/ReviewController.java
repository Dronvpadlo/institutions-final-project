package com.example.institutionsfinalproject.controller;

import com.example.institutionsfinalproject.entity.dto.ResponseDTO;
import com.example.institutionsfinalproject.entity.dto.ReviewDTO;
import com.example.institutionsfinalproject.service.ReviewService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/review")
public class ReviewController {

    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @GetMapping
    public ResponseEntity<ResponseDTO<ReviewDTO>> getReviews(@RequestParam(defaultValue = "0") int skip, @RequestParam(defaultValue = "10") int limit){
        ResponseDTO<ReviewDTO> reviews = reviewService.getAllReviews(skip, limit);
        return new ResponseEntity<>(reviews, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<ReviewDTO> postReview(@RequestBody ReviewDTO reviewDTO){
        ReviewDTO newReview = reviewService.createReview(reviewDTO);
        return new ResponseEntity<>(newReview, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReview(@PathVariable String id){
        reviewService.deleteReview(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReviewDTO> getReviewById(@PathVariable String id){
        return reviewService.getReviewById(id)
                .map(reviewDTO -> new ResponseEntity<>(reviewDTO, HttpStatus.OK))
                .orElseGet(()-> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ReviewDTO> putReview(@PathVariable String id, @RequestBody ReviewDTO reviewDTO){
        return reviewService.updateReview(id, reviewDTO)
                .map(updatesReview -> new ResponseEntity<>(updatesReview, HttpStatus.OK))
                .orElseGet(()-> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ReviewDTO> patchReview(@PathVariable String id, @RequestBody Map<String, Object> updates){
        return reviewService.patchReview(id, updates)
                .map(updatesReview -> new ResponseEntity<>(updatesReview, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));

    }

}
