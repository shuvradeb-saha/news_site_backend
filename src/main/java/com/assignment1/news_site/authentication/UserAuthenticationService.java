package com.assignment1.news_site.authentication;

import com.assignment1.news_site.model.User;
import com.assignment1.news_site.service.UserService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserAuthenticationService implements UserDetailsService {

	private UserService userService;

	public UserAuthenticationService(UserService userService) {
		this.userService = userService;
	}


	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		if(!userService.getUserByEmail(email).isPresent())
			throw new UsernameNotFoundException("Username not found for " + email);
		User user = userService.getUserByEmail(email).get();
		return new UserPrinciple(user);
	}
}
