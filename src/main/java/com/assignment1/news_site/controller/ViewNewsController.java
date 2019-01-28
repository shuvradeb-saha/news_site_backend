package com.assignment1.news_site.controller;

import com.assignment1.news_site.exception.ResourceNotFoundException;
import com.assignment1.news_site.model.News;
import com.assignment1.news_site.service.NewsService;
import com.fasterxml.jackson.xml.XmlMapper;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

import java.io.IOException;

@RestController
public class ViewNewsController {
	private NewsService newsService;

	public ViewNewsController(NewsService newsService) {
		this.newsService = newsService;
	}


	@GetMapping("/view")
	public News viewThisNews(@RequestParam("id") Integer id) {
		boolean not_found = false;
		News showNews = null;
		if (id == null) {
			not_found = true;
		} else {
			showNews = newsService.findNewsById(id);

			if (showNews == null) {
				not_found = true;
			}
		}
		if (not_found)
			throw new ResourceNotFoundException();

		return showNews;
	}

	@GetMapping("view/json")
	public ResponseEntity getJSON(@RequestParam("id") Integer id) {
		if (id == null)
			throw new ResourceNotFoundException();
		News news = newsService.findNewsById(id);
		if (news == null)
			return new ResponseEntity<>(news, HttpStatus.NOT_FOUND);
		else
			return new ResponseEntity<>(news, HttpStatus.OK);
	}

	@RequestMapping("view/xml")
	@ResponseBody
	public void getXML(@ModelAttribute("id") Integer id, HttpServletResponse response) {
		News showNews = newsService.findNewsById(id);
		XmlMapper mapper = new XmlMapper();
		String xml = null;
		try {
			xml = mapper.writeValueAsString(showNews);
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			response.getWriter().print(xml);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


}
