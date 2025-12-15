package com.example.institutionsfinalproject.service;

import com.example.institutionsfinalproject.entity.NewsEntity;
import com.example.institutionsfinalproject.entity.NewsType;
import com.example.institutionsfinalproject.entity.dto.NewsDTO;
import com.example.institutionsfinalproject.entity.dto.NewsFilterDTO;
import com.example.institutionsfinalproject.entity.dto.ResponseDTO;
import com.example.institutionsfinalproject.mapper.NewsMapper;
import com.example.institutionsfinalproject.repository.InstitutionRepository;
import com.example.institutionsfinalproject.repository.NewsRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
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
    private final MongoTemplate mongoTemplate;

    public NewsService(NewsRepository newsRepository, NewsMapper newsMapper, InstitutionRepository institutionRepository, MongoTemplate mongoTemplate){
        this.newsRepository = newsRepository;
        this.newsMapper = newsMapper;
        this.institutionRepository = institutionRepository;
        this.mongoTemplate = mongoTemplate;
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

    public ResponseDTO<NewsDTO> getAllNews(int skip, int limit){
        Pageable pageable = PageRequest.of(skip/limit, limit);
        Page<NewsEntity> newsPage = newsRepository.findAll(pageable);

        List<NewsDTO> news = newsPage.getContent()
                .stream()
                .map(newsMapper::toDto)
                .collect(Collectors.toList());
        return new ResponseDTO<>(news, newsPage.getTotalElements(), skip, limit);
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
                           case "date": news.setDate((LocalTime) value); break;
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

    public ResponseDTO<NewsDTO> getFilteredNews(NewsFilterDTO filterDTO, int skip, int limit){
        int page = skip/limit;
        Sort.Direction direction = filterDTO.getSortDirection().equalsIgnoreCase("desc")
                ? Sort.Direction.DESC
                : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, limit, Sort.by(direction, filterDTO.getSortBy()));

        Query query = new Query().with(pageable);
        List<Criteria> criteriaList = new ArrayList<>();

        if (filterDTO.getType() != null){
            criteriaList.add(Criteria.where("type").is(filterDTO.getType()));
        }

//        criteriaList.add(Criteria.where("moderationStatus").is(ModerationStatus.APPROVED));

        if (!criteriaList.isEmpty()) {
            query.addCriteria(new Criteria().andOperator(criteriaList.toArray(new Criteria[0])));
        }

        List<NewsEntity> news = mongoTemplate.find(query, NewsEntity.class);
        long total = mongoTemplate.count(query.skip(-1).limit(-1), NewsEntity.class);

        List<NewsDTO> newsDTOs = news.stream().map(newsMapper::toDto).collect(Collectors.toList());

        return new ResponseDTO<>(newsDTOs, total, skip, limit);
    }
}
