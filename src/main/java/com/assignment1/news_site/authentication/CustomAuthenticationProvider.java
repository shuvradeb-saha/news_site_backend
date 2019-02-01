package com.assignment1.news_site.authentication;

import com.assignment1.news_site.exception.AuthenticationCredException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {

	private UserAuthenticationService userAuthenticationService;
	private BCryptPasswordEncoder bCryptPasswordEncoder;


	@Autowired
	public CustomAuthenticationProvider(UserAuthenticationService userAuthenticationService, BCryptPasswordEncoder bCryptPasswordEncoder) {
		this.userAuthenticationService = userAuthenticationService;
		this.bCryptPasswordEncoder = bCryptPasswordEncoder;
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return (UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication));
	}

	@Override
	public Authentication authenticate(Authentication authentication)  {
		String emailId = authentication.getName();
		String password = (String) authentication.getCredentials();

		if (emailId.length()==0 || password.length()==0){
			System.out.println("dafjdh");
		}

		UserDetails user = this.userAuthenticationService.loadUserByUsername(emailId);

		if (user == null) {
			throw new UsernameNotFoundException("Username not found.");
		}

		if (!bCryptPasswordEncoder.matches(password, user.getPassword())) {
			throw new UsernameNotFoundException("Wrong password.");
		}

		Collection<? extends GrantedAuthority> authorities = user.getAuthorities();
		return new UsernamePasswordAuthenticationToken(user, password, authorities);
	}
}
