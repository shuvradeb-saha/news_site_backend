package com.assignment1.news_site.controller;

import com.assignment1.news_site.exception.ResourceNotFoundException;
import com.assignment1.news_site.model.News;
import com.assignment1.news_site.model.User;
import com.assignment1.news_site.service.NewsService;
import com.assignment1.news_site.service.UserService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
public class NewsController {
	private NewsService newsService;
	private UserService userService;

	public NewsController(NewsService newsService, UserService userService) {
		this.newsService = newsService;
		this.userService = userService;
	}

	@GetMapping(value = "view/json")
	public ResponseEntity getNewsJson(@RequestParam("id") Integer id) {
		News news = getCheckedNews(id);
		return new ResponseEntity<>(news, HttpStatus.OK);
	}

	@GetMapping(value = "view/xml", produces = { "application/xml"})
	public ResponseEntity getNewsXml(@RequestParam("id") Integer id) {
		News news = getCheckedNews(id);
		return new ResponseEntity<>(news, HttpStatus.OK);
	}

	@PostMapping("/news")
	public ResponseEntity saveSubmittedNews(@Valid @RequestBody News news,
											BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			return new ResponseEntity<>("Invalid News Information", HttpStatus.BAD_REQUEST);
		}
		System.out.println("News = " + news);
		User user = userService.getAUthenticatedUser();
		System.out.println("user = " + user);
		news.setAuthor(user.getFullName());
		news.setUserId(user.getId());
		System.out.println("News = " + news);

		newsService.saveNews(news);
		return new ResponseEntity<>("News Saved Successfully", HttpStatus.OK);
	}

	@GetMapping("/news")
	public ResponseEntity getNewsForEdit(@RequestParam("id") Integer id) {
		User user = userService.getAUthenticatedUser();
		Integer userId = user.getId();
		News news = getCheckedNews(id);
		if (!userId.equals(news.getUserId())) {
			return new ResponseEntity<>("You Have No Permission TO Update This News", HttpStatus.FORBIDDEN);
		}
		if (!news.getUserId().equals(userId)) {
			return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);
		} else {
			return new ResponseEntity<>(news, HttpStatus.OK);
		}
	}


	@PutMapping("/news")
	public ResponseEntity updateNews(@Valid @RequestBody News news, BindingResult bindingResult, @RequestParam("id") Integer id) {
		System.out.println("puttt  ");
		User user = userService.getAUthenticatedUser();
		Integer userId = user.getId();

		if (bindingResult.hasErrors()) {
			return new ResponseEntity<>("Invalid News Information", HttpStatus.BAD_REQUEST);
		}
		if (id == null) {
			return new ResponseEntity<>("Invalid News Information", HttpStatus.NOT_FOUND);
		} else if (!newsService.checkIfNewsExists(id)) {
			return new ResponseEntity<>("News Does Not Exists", HttpStatus.BAD_REQUEST);
		} else if (!userId.equals(getCheckedNews(id).getUserId())) {
			return new ResponseEntity<>("You Have No Permission TO Update This News", HttpStatus.FORBIDDEN);
		} else {
			news.setId(id);
			news.setAuthor(user.getFullName());
			news.setUserId(userId);
			System.out.println(news);
			System.out.println("user - "+news.getUserId());
			newsService.saveNews(news);
			return new ResponseEntity<>("News Updated Successfully", HttpStatus.OK);
		}
	}

	@DeleteMapping("/news")
	public ResponseEntity removeNews(@RequestParam("id") Integer id) {
		User user = userService.getAUthenticatedUser();
		Integer userId = user.getId();
		News news = getCheckedNews(id);
		if (news.getUserId().equals(userId)) {
			if (newsService.deleteNewsById(id))
				return new ResponseEntity<>("Deleted Successfully", HttpStatus.OK);
		}
		return new ResponseEntity<>("You are not allowed to remove", HttpStatus.FORBIDDEN);
	}

	private News getCheckedNews(Integer id) {
		if (!newsService.findNewsById(id).isPresent()) {
			throw new ResourceNotFoundException();
		}
		return newsService.findNewsById(id).get();
	}


}
