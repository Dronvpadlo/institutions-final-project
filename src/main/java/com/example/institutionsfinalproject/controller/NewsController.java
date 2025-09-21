package com.example.institutionsfinalproject.controller;

import com.example.institutionsfinalproject.entity.dto.NewsDTO;
import com.example.institutionsfinalproject.service.NewsService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

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

    @GetMapping("/{id}")
    public ResponseEntity<NewsDTO> getNewsById(@PathVariable String id){
        return newsService.getNewsById(id)
                .map(newsDTO -> new ResponseEntity<>(newsDTO, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PutMapping("/{id}")
    public ResponseEntity<NewsDTO> putNews(@PathVariable String id, @RequestBody NewsDTO newsDTO){
        return newsService.putNews(id,  newsDTO)
                .map(updatedNews -> new ResponseEntity<>(updatedNews, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));

    }

    @PatchMapping("/{id}")
    public ResponseEntity<NewsDTO> patchNews(@PathVariable String id, @RequestBody Map<String, Object> updates){
        return newsService.patchNews(id, updates)
                .map(updatedNews -> new ResponseEntity<>(updatedNews, HttpStatus.OK))
                .orElseGet(()-> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

}
