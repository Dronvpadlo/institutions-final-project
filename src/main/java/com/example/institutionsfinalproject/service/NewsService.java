package com.example.institutionsfinalproject.service;

import com.example.institutionsfinalproject.entity.NewsEntity;
import com.example.institutionsfinalproject.entity.NewsType;
import com.example.institutionsfinalproject.entity.dto.NewsDTO;
import com.example.institutionsfinalproject.mapper.NewsMapper;
import com.example.institutionsfinalproject.repository.InstitutionRepository;
import com.example.institutionsfinalproject.repository.NewsRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
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

    private void removeNewsFromRelatedEntities(String newsId, String institutionId){
        institutionRepository.findById(institutionId).ifPresent(institution -> {
            List<String> newsIds = institution.getNewsIds();
            if (newsIds != null){
                newsIds.remove(newsId);
                institution.setNewsIds(newsIds);
                institutionRepository.save(institution);
            }
        });
    }

    public List<NewsDTO> getAllNews(){
        List<NewsEntity> news = newsRepository.findAll();
        return news.stream()
                .map(newsMapper::toDto)
                .collect(Collectors.toList());
    }

    public void deleteNews(String id){
        newsRepository.findById(id).ifPresent(news -> {
            removeNewsFromRelatedEntities(news.getId(), news.getInstitutionId());
            newsRepository.deleteById(id);
        });
    }

    public Optional<NewsDTO> getNewsById(String id){
        return newsRepository.findById(id)
                .map(newsMapper::toDto);
    }

    public Optional<NewsDTO> putNews(String id, NewsDTO newsDTO){
        return newsRepository.findById(id)
                .map(existedNews -> {
                    existedNews.setType(newsDTO.getType());
                    existedNews.setDate(newsDTO.getDate());
                    existedNews.setInstitutionId(newsDTO.getInstitutionId());
                    existedNews.setDescription(newsDTO.getDescription());
                    existedNews.setTitle(newsDTO.getTitle());
                    newsRepository.save(existedNews);
                    return newsMapper.toDto(existedNews);
                });
    }

    public Optional<NewsDTO> patchNews(String id, Map<String, Object> updates){
        return newsRepository.findById(id)
                .map(news -> {
                   updates.forEach((key, value) ->{
                       switch (key){
                           case "title": news.setTitle((String) value); break;
                           case "date": news.setDate((String) value); break;
                           case "description": news.setDescription((String) value); break;
                           case "institutionId": news.setInstitutionId((String) value); break;
                           case "type":
                               try {
                                   news.setType(NewsType.valueOf((String) value));
                               } catch (Exception e) {
                                   throw new IllegalArgumentException("Invalid NewsType value: " + value);
                               }
                       }
                   });
                   NewsEntity updatedNews = newsRepository.save(news);
                   return newsMapper.toDto(updatedNews);
                });
    }

}
