package com.assignment1.news_site.controller;


import com.assignment1.news_site.exception.ResourceNotFoundException;
import com.assignment1.news_site.model.News;
import com.assignment1.news_site.model.User;
import com.assignment1.news_site.service.NewsService;
import com.assignment1.news_site.service.UserService;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;


@RestController
public class NewsController {
	private NewsService newsService;
	private UserService userService;

	public NewsController(NewsService newsService, UserService userService) {
		this.newsService = newsService;
		this.userService = userService;
	}

	@GetMapping("view/json")
	public ResponseEntity getNews(@RequestParam("id") Integer id) throws JSONException {

		if (id == null)
			throw new ResourceNotFoundException();
		News news = newsService.findNewsById(id);

		JSONObject newsJson = new JSONObject();
		newsJson.put("id", news.getId());
		newsJson.put("title", news.getTitle());
		newsJson.put("body", news.getBody());
		newsJson.put("date", news.getDate());
		newsJson.put("author", news.getAuthor());
		return new ResponseEntity<>(newsJson.toString(), HttpStatus.OK);
	}


	@PostMapping("user/submit-news/{userId}")
	public ResponseEntity saveSubmittedNews(@PathVariable("userId") Integer userId, @RequestBody News news) {
		if (checkAccess(userId))
			return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);
		news.setUserId(userId);
		newsService.saveNews(news);
		return new ResponseEntity<>("News Saved Successfully", HttpStatus.OK);
	}

	@GetMapping("user/edit/{userId}")
	public ResponseEntity getNewsForEdit(@PathVariable("userId") Integer userId, @RequestParam("id") Integer id) {
		if (checkAccess(userId))
			return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);
		News news = newsService.findNewsById(id);
		if (news == null) {
			return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
		} else if (!news.getUserId().equals(userId)) {
			return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);
		} else {
			return new ResponseEntity<>(news, HttpStatus.OK);
		}
	}


	@PutMapping("user/update-news/{userId}")
	public ResponseEntity updateNews(@RequestBody News news,@PathVariable("userId") Integer userId) {
		if (checkAccess(userId))
			return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);
		if (news == null) {
			return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
		} else if (!userId.equals(news.getUserId())) {
			return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);
		} else {
			News updatedNews = newsService.saveNews(news);
			return new ResponseEntity<>(updatedNews, HttpStatus.OK);
		}
	}

	@DeleteMapping("user/remove/{userId}")
	public ResponseEntity removeNews(@RequestParam("id") Integer id,@PathVariable("userId")Integer userId) {
		if (checkAccess(userId))
			return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);
		if (newsService.findNewsById(id).getUserId().equals(userId)){
			if(newsService.deleteNewsById(id))
				return new ResponseEntity<>(HttpStatus.OK);
		}
		return new ResponseEntity<>(HttpStatus.FORBIDDEN);
	}

	private boolean checkAccess(Integer userId){
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		System.out.println(auth.toString());
		System.out.println(auth.getName());
		User user = userService.getUserByEmail(auth.getName());
		System.out.println(user.toString());

		return !user.getId().equals(userId);


	}

}
