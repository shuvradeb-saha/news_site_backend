package com.assignment1.news_site.controller;

import com.assignment1.news_site.exception.ResourceNotFoundException;
import com.assignment1.news_site.model.News;
import com.assignment1.news_site.service.NewsService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;




@RestController
public class CreateNewsController {
	private NewsService newsService;

	public CreateNewsController(NewsService newsService) {
		this.newsService = newsService;
	}


	@PostMapping("/submit-news")
	public ResponseEntity saveSubmittedNews(@RequestBody News news) {
		//System.out.println(news.toString()+news.getDate());
		newsService.saveNews(news);
		return new ResponseEntity<>("News Saved Successfully", HttpStatus.OK);

	}

	@GetMapping("/edit")
	public ResponseEntity getNewsForEdit(@RequestParam("id") Integer id){
		News news = newsService.findNewsById(id);
		if(news == null)
			return new ResponseEntity<>(news,HttpStatus.NOT_FOUND);
		else
			return new ResponseEntity<>(news,HttpStatus.OK);
	}



	@PutMapping("/update-news")
	public void updateNews(@RequestBody News news, @RequestParam("id") Integer id) {
		News updatedNews = newsService.saveNews(news);
		//return new ResponseEntity<>(updatedNews,HttpStatus.OK);
	}

	@DeleteMapping("/remove")
	public void removeNews(@RequestParam("id") Integer id) {
		newsService.deleteNewsById(id);
	}

}
