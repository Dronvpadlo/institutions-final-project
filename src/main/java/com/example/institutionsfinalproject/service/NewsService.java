package com.example.institutionsfinalproject.service;

import com.example.institutionsfinalproject.entity.NewsEntity;
import com.example.institutionsfinalproject.entity.dto.NewsDTO;
import com.example.institutionsfinalproject.mapper.NewsMapper;
import com.example.institutionsfinalproject.repository.InstitutionRepository;
import com.example.institutionsfinalproject.repository.NewsRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class NewsService {
    private final NewsRepository newsRepository;
    private final InstitutionRepository institutionRepository;
    private final NewsMapper newsMapper;

    public NewsService(NewsRepository newsRepository, NewsMapper newsMapper, InstitutionRepository institutionRepository){
        this.newsRepository = newsRepository;
        this.newsMapper = newsMapper;
        this.institutionRepository = institutionRepository;
    }

    public NewsDTO createNews(NewsDTO newsDTO){
        NewsEntity newsEntity = newsMapper.toEntity(newsDTO);
        NewsEntity savedNews = newsRepository.save(newsEntity);

        institutionRepository.findById(savedNews.getInstitutionId()).ifPresent(institution -> {
            List<String> newsIds = institution.getNewsIds();
            if (newsIds == null){
                newsIds = new ArrayList<>();
            }
            newsIds.add(savedNews.getId());
            institution.setNewsIds(newsIds);

            institutionRepository.save(institution);
        });

        return newsMapper.toDto(savedNews);
    }

    public List<NewsDTO> getAllNews(){
        List<NewsEntity> news = newsRepository.findAll();
        return news.stream()
                .map(newsMapper::toDto)
                .collect(Collectors.toList());
    }

    public void deleteNews(String id){
        newsRepository.deleteById(id);
    }
}
