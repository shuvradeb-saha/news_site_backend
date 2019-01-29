package com.assignment1.news_site.controller;

import com.assignment1.news_site.exception.ResourceNotFoundException;
import com.assignment1.news_site.model.News;
import com.assignment1.news_site.service.NewsService;
import com.fasterxml.jackson.core.JsonProcessingException;


import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
public class ViewNewsController {
	private NewsService newsService;

	public ViewNewsController(NewsService newsService) {
		this.newsService = newsService;
	}

	@GetMapping("view/json")
	public ResponseEntity getJSON(@RequestParam("id") Integer id) throws JsonProcessingException, JSONException {
		if (id == null)
			throw new ResourceNotFoundException();
		News news = newsService.findNewsById(id);
		System.out.println(news.toString() + news.getDate());
		JSONObject newsJson = new JSONObject();
		newsJson.put("id",news.getId());
		newsJson.put("title",news.getTitle());
		newsJson.put("body",news.getBody());
		newsJson.put("date",news.getDate());
		newsJson.put("author",news.getAuthor());
		if (news == null)
			return new ResponseEntity<>(news, HttpStatus.NOT_FOUND);
		else
			return new ResponseEntity<>(newsJson.toString(), HttpStatus.OK);
	}



}
