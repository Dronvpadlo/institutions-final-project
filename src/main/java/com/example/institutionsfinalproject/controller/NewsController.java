package com.example.institutionsfinalproject.controller;

import com.example.institutionsfinalproject.entity.dto.NewsDTO;
import com.example.institutionsfinalproject.service.NewsService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/news")
public class NewsController {

    private final NewsService newsService;

    public NewsController(NewsService newsService){
        this.newsService = newsService;
    }
    @PostMapping()
    public ResponseEntity<NewsDTO> createNews(@RequestBody NewsDTO newsDTO){
        NewsDTO newNews = newsService.createNews(newsDTO);
        return new ResponseEntity<>(newNews, HttpStatus.CREATED);
    }

    @GetMapping()
    public ResponseEntity<List<NewsDTO>> getAllNews(){
        List<NewsDTO> newsDTO = newsService.getAllNews();
        return new ResponseEntity<>(newsDTO, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteNews(@PathVariable String id){
        newsService.deleteNews(id);
        return new ResponseEntity<>("News was deleted successful", HttpStatus.NO_CONTENT);
    }
}
