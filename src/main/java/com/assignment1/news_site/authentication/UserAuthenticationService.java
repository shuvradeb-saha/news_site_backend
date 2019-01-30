package com.assignment1.news_site.authentication;

import com.assignment1.news_site.model.User;
import com.assignment1.news_site.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserAuthenticationService implements UserDetailsService {
	@Autowired
	private UserService userService;


	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		User user = userService.getUserByEmail(email);
		if (user == null) {
			throw new UsernameNotFoundException("Username not found for " + email);
		}
		return new UserPrinciple(user);

	}
}
