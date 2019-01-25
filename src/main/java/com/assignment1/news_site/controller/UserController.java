package com.assignment1.news_site.controller;

import com.assignment1.news_site.model.User;
import com.assignment1.news_site.service.UserService;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@RestController
public class UserController {
	private UserService userService;

	public UserController(UserService userService) {
		this.userService = userService;
	}

	@GetMapping("/signup-page")
	public ModelAndView getSignUpPage(HttpSession session) {
		if (session.getAttribute("user") != null) {
			return new ModelAndView("redirect:/");
		}

		ModelAndView modelAndView = new ModelAndView("registration");
		modelAndView.addObject("user", new User());
		return modelAndView;
	}

	@GetMapping("/login-page")
	public ModelAndView getLoginPage(HttpSession session) {
		if (session.getAttribute("user") != null) {
			return new ModelAndView("redirect:/");
		}
		ModelAndView modelAndView = new ModelAndView("login");
		modelAndView.addObject("user", new User());
		return modelAndView;
	}

	@PostMapping("/signup")
	public String submitSignUpform(@Valid @ModelAttribute User user, BindingResult bindingResult, Model model, @ModelAttribute("confirmPass") String confirmPass, HttpSession session) {

		if (session.getAttribute("user") != null) {
			return "redirect:/";
		}

		boolean error = false;
		if (bindingResult.hasErrors()) {
			error = true;

		} else if (userService.checkEmailExists(user.getEmail())) {
			error = true;
			model.addAttribute("emailError", "Email You Entered Already Exists");
		} else if (!user.getPassword().equals(confirmPass)) {
			error = true;
			model.addAttribute("passwordError", "Confirm Password Doesnot Match");
		}
		if (error) {
			return "registration";
		}
		userService.saveUser(user);
		model.addAttribute("saved", "Registration Completed. Now Login Please");
		return "login";
	}

	@PostMapping("/login")
	public String  loginVerification(@RequestHeader String userInfo) {
		System.out.println(userInfo);
		return userInfo;

/*
		User login_user = userService.getUserByEmail(user.getEmail());
		if (login_user == null) {
			model.addAttribute("notFound", "Email Does Not Match. Please Sign Up First To Login");
			return "login";
		}


		if (!userService.matchPassword(user.getPassword(), login_user.getPassword())) {
			model.addAttribute("wrongPassword", "Password You Entered Is Incorrect");
			return "login";
		}

		session.setAttribute("login", true);
		session.setAttribute("user", login_user);
		return "redirect:/";
	*/
	}


	@PostMapping("/logout")
	public String logout(HttpSession session) {
		session.invalidate();
		return "redirect:/";
	}


}
