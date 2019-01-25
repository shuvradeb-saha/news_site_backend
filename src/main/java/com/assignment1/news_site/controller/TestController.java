package com.assignment1.news_site.controller;

import com.assignment1.news_site.service.NewsService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class TestController {
	private NewsService newsService;

	public TestController(NewsService newsService) {
		this.newsService = newsService;
	}
	private static final int BUTTONS_TO_SHOW = 3;
	private static final int INITIAL_PAGE = 0;
	private static final int INITIAL_PAGE_SIZE = 5;
	private static final int[] PAGE_SIZES = {5, 10};

	@RequestMapping("/test")
	public ModelAndView testThymeleaf() {
		ModelAndView modelAndView = new ModelAndView("for_test");
		modelAndView.addObject("a", 1);
		return modelAndView;
	}


}
