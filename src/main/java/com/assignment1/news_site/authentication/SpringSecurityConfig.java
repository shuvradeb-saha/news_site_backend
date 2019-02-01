package com.assignment1.news_site.authentication;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@Configuration
@EnableWebSecurity
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {
	private final UserAuthenticationService userAuthenticationService;
	private final CustomAuthenticationProvider authenticationProvider;

	@Autowired
	public SpringSecurityConfig(CustomAuthenticationProvider authenticationProvider, UserAuthenticationService userAuthenticationService) {
		this.authenticationProvider = authenticationProvider;
		this.userAuthenticationService = userAuthenticationService;
	}

	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		auth.authenticationProvider(this.authenticationProvider).userDetailsService(this.userAuthenticationService);
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
			.cors().and().csrf().disable().authorizeRequests()
			.antMatchers("/submit-news").authenticated()
			.antMatchers("/user/**").authenticated().and().
			formLogin().failureHandler(new RestAuthenticationFailureHandler()).and().httpBasic();
	}


}