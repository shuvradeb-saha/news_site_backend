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

	@GetMapping(value = "view/json", produces = "application/json")
	public ResponseEntity getNews(@RequestParam("id") Integer id) {
		if (!newsService.findNewsById(id).isPresent())
			return new ResponseEntity<>("Invalid News Id", HttpStatus.NOT_FOUND);
		News news = newsService.findNewsById(id).get();
		return new ResponseEntity<>(news, HttpStatus.OK);
	}

	@PostMapping("user/submit-news/{userId}")
	public ResponseEntity saveSubmittedNews(@PathVariable("userId") Integer userId, @Valid @RequestBody News news,
											BindingResult bindingResult) {
		System.out.println("News: ---- "+news);
		if (checkAccess(userId))
			return new ResponseEntity<>("This user id is not Authenticated", HttpStatus.FORBIDDEN);
		if (bindingResult.hasErrors()) {
			return new ResponseEntity<>("Invalid News Information", HttpStatus.BAD_REQUEST);
		}
		news.setAuthor(userService.getUserNameById(userId));
		news.setUserId(userId);

		newsService.saveNews(news);
		return new ResponseEntity<>("News Saved Successfully", HttpStatus.OK);
	}

	@GetMapping("user/edit/{userId}")
	public ResponseEntity getNewsForEdit(@PathVariable("userId") Integer userId, @RequestParam("id") Integer id) {
		if (checkAccess(userId))
			return new ResponseEntity<>("This user id is not Authenticated", HttpStatus.FORBIDDEN);
		System.out.println("abc");
		News news = getCheckedNews(id);
		System.out.println("news: --"+news);
		if (!userId.equals(news.getUserId())) {
			return new ResponseEntity<>("You Have No Permission TO Update This News", HttpStatus.FORBIDDEN);
		}
		if (!news.getUserId().equals(userId)) {
			return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);
		} else {
			return new ResponseEntity<>(news, HttpStatus.OK);
		}
	}


	@PutMapping("user/update-news/{userId}")
	public ResponseEntity updateNews(@Valid @RequestBody News news, @PathVariable("userId") Integer userId,
									 BindingResult bindingResult) {
		if (checkAccess(userId)) {
			return new ResponseEntity<>("This user id is not Authenticated", HttpStatus.FORBIDDEN);
		}
		if (bindingResult.hasErrors()) {
			return new ResponseEntity<>("Invalid News Information", HttpStatus.BAD_REQUEST);
		}
		if (news.getId() == null) {
			return new ResponseEntity<>("Invalid News Information", HttpStatus.NOT_FOUND);
		} else if (!newsService.checkIfNewsExists(news.getId())) {
			return new ResponseEntity<>("News Does Not Exists", HttpStatus.BAD_REQUEST);
		} else if (!userId.equals(news.getUserId())) {
			return new ResponseEntity<>("You Have No Permission TO Update This News", HttpStatus.FORBIDDEN);
		} else {
			news.setAuthor(getCheckedNews(news.getId()).getAuthor());
			news.setUserId(userId);
			newsService.saveNews(news);
			return new ResponseEntity<>("News Updated Successfully", HttpStatus.OK);
		}
	}

	@DeleteMapping("user/remove/{userId}")
	public ResponseEntity removeNews(@RequestParam("id") Integer id, @PathVariable("userId") Integer userId) {
		if (checkAccess(userId))
			return new ResponseEntity<>("This user id is not Authenticated", HttpStatus.FORBIDDEN);
		System.out.println("abc");
		News news = getCheckedNews(id);
		System.out.println("news: --"+news);
		if (news.getUserId().equals(userId)) {
			if (newsService.deleteNewsById(id))
				return new ResponseEntity<>(HttpStatus.OK);
		}
		return new ResponseEntity<>(HttpStatus.FORBIDDEN);
	}

	private News getCheckedNews(Integer id) {
		if (!newsService.findNewsById(id).isPresent()) {
			System.out.println("getCheckedNews");
			throw new ResourceNotFoundException();
		}
		return newsService.findNewsById(id).get();
	}

	private boolean checkAccess(Integer userId) {
		User user = userService.getAUthenticatedUser();
		return !user.getId().equals(userId);
	}

}
