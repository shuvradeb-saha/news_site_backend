package com.assignment1.news_site.controller;

import com.assignment1.news_site.model.News;
import com.assignment1.news_site.service.NewsService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import javax.servlet.http.HttpSession;



@RestController
public class CreateNewsController {
	private NewsService newsService;

	public CreateNewsController(NewsService newsService) {
		this.newsService = newsService;
	}


	@PostMapping("/submit-news")
	public News saveSubmittedNews(@RequestBody News news) {
		//System.out.println(news.toString()+news.getDate());
		return newsService.saveNews(news);

	}


	@PutMapping("/updateNews")
	public News updateNews(@RequestBody News news, @RequestParam("id") Integer id) {
		System.out.println(news.toString()+news.getDate());
		news.setId(id);
		return	newsService.saveNews(news);
	}

	@DeleteMapping("/remove")
	public boolean removeNews(@RequestParam("id") Integer id) {
		return newsService.deleteNewsById(id);
	}

}
