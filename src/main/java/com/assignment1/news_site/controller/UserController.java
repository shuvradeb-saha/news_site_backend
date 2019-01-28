package com.assignment1.news_site.controller;

import com.assignment1.news_site.model.User;
import com.assignment1.news_site.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import javax.servlet.http.HttpSession;


@RestController
public class UserController {
	private UserService userService;

	public UserController(UserService userService) {
		this.userService = userService;
	}


	@PostMapping("/signup")
	public ResponseEntity submitSignUpform(@RequestBody User user) {
		if (userService.checkEmailExists(user.getEmail())) {
			return new ResponseEntity<>("Email You Entered Already Exists", HttpStatus.ALREADY_REPORTED);
		}
		userService.saveUser(user);
		return new ResponseEntity<>("Registration completed", HttpStatus.OK);

	}


	@PostMapping("/login")
	public ResponseEntity loginVerification(@RequestBody User user) {
		System.out.println(user.toString());

		User login_user = userService.getUserByEmail(user.getEmail());

		if (login_user == null) {
			return new ResponseEntity<>("Email Not found", HttpStatus.OK);
		}

		if (!userService.matchPassword(user.getPassword(), login_user.getPassword())) {
			return new ResponseEntity<>("Password Not matched", HttpStatus.OK);
		}
		return new ResponseEntity<>("login", HttpStatus.OK);
	}

	@PostMapping("/logout")
	public String logout(HttpSession session) {
		session.invalidate();
		return "redirect:/";
	}

}
