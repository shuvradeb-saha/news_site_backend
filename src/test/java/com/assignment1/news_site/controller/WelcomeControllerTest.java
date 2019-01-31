package com.assignment1.news_site.controller;


import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@WebMvcTest(value = WelcomeController.class, secure = false)
public class WelcomeControllerTest {



}