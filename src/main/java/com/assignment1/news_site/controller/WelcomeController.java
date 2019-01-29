package com.assignment1.news_site.controller;


import com.assignment1.news_site.model.News;
import com.assignment1.news_site.model.PagerModel;
import com.assignment1.news_site.service.NewsService;
import org.json.JSONArray;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


import javax.servlet.http.HttpSession;

import java.util.List;
import java.util.Optional;

@RestController
public class WelcomeController {


	private static final int BUTTONS_TO_SHOW = 3;
	private static final int INITIAL_PAGE = 0;
	private static final int INITIAL_PAGE_SIZE = 3;
	private static final int[] PAGE_SIZES = {3, 5, 6, 10};


	private NewsService newsService;

	public WelcomeController(NewsService newsService) {
		this.newsService = newsService;
	}




	@GetMapping("/")
	public ResponseEntity getWelcomePage(@RequestParam("pageSize") Optional<Integer> pageSize,
										 @RequestParam("page") Optional<Integer> page, HttpSession session) throws JSONException, NoSuchFieldException {

		int evalPageSize = pageSize.orElse(INITIAL_PAGE_SIZE);
		int evalPage = (page.orElse(0) < 1) ? INITIAL_PAGE : page.get();
		Page<News> newsList = newsService.findPages(PageRequest.of(evalPage, evalPageSize, Sort.Direction.DESC, "date"));
		PagerModel pager = new PagerModel(newsList.getTotalPages(), newsList.getNumber(), BUTTONS_TO_SHOW);


		JSONObject newsJson = new JSONObject();
		JSONArray newsJsonArray = new JSONArray();

		for (News news : newsList) {
			JSONObject singleFieldObject = new JSONObject();
			singleFieldObject.put("id", news.getId());
			singleFieldObject.put("title", news.getTitle());
			singleFieldObject.put("author", news.getAuthor());
			singleFieldObject.put("date", news.getDate());
			singleFieldObject.put("body",news.getBody());
			newsJsonArray.put(singleFieldObject);


		}

		newsJson.put("newsList", newsJsonArray);
		newsJson.put("totalPages", newsList.getTotalPages());
		newsJson.put("number", newsList.getNumber());
		newsJson.put("pagerStart", pager.getStartPage());
		newsJson.put("pagerEnd", pager.getEndPage());

		return new ResponseEntity<>(newsJson.toString(), HttpStatus.OK);


	}
}
