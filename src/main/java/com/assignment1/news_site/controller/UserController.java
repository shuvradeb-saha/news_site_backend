package com.assignment1.news_site.controller;

import com.assignment1.news_site.model.User;
import com.assignment1.news_site.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


@RestController
public class UserController {
	private UserService userService;

	public UserController(UserService userService) {
		this.userService = userService;
	}


	@PostMapping("/signup")
	public ResponseEntity submitSignUpform(@Valid @RequestBody User user, BindingResult bindingResult) {
		if (bindingResult.hasErrors()){
			return new ResponseEntity<>("Input format Not Supported", HttpStatus.CONFLICT);
		}
		if (userService.checkEmailExists(user.getEmail())) {
			return new ResponseEntity<>("Email You Entered Already Exists", HttpStatus.ALREADY_REPORTED);
		}
		userService.saveUser(user);
		return new ResponseEntity<>("Registration completed", HttpStatus.OK);

	}

	@GetMapping("/user/login")
	public ResponseEntity afterlogin(){
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		User user = userService.getUserByEmail(auth.getName());
		return new ResponseEntity<>(user,HttpStatus.OK);
	}


}
