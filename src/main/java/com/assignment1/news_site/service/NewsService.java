package com.assignment1.news_site.service;

import com.assignment1.news_site.exception.ResourceNotFoundException;
import com.assignment1.news_site.model.News;
import com.assignment1.news_site.repository.NewsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class NewsService {

	private NewsRepository newsRepository;

	@Autowired
	public NewsService(NewsRepository newsRepository) {
		this.newsRepository = newsRepository;
	}

	public void saveNews(News news) {
		newsRepository.saveAndFlush(news);
	}

	public Optional<News> findNewsById(Integer id) {
		return newsRepository.findById(id);
	}

	public Page<News> findPages(Pageable pageRequest) {
		return newsRepository.findAll(pageRequest);
	}

	public boolean deleteNewsById(Integer id) {
		if (!newsRepository.existsById(id)) {
			throw new ResourceNotFoundException();
		} else {
			newsRepository.deleteById(id);
			return true;
		}
	}

	public boolean checkIfNewsExists(Integer id){
		return newsRepository.existsById(id);
	}


}
