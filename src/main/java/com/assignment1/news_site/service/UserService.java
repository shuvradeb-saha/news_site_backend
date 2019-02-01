package com.assignment1.news_site.service;


import com.assignment1.news_site.exception.ResourceNotFoundException;
import com.assignment1.news_site.exception.UsernameNotFoundException;
import com.assignment1.news_site.model.User;
import com.assignment1.news_site.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
	private final BCryptPasswordEncoder bCryptPasswordEncoder;

	private UserRepository userRepository;
	@Autowired
	public UserService(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
		this.userRepository = userRepository;
		this.bCryptPasswordEncoder = bCryptPasswordEncoder;
	}

	public boolean checkEmailExists(String email){
		if (userRepository.count()==0)
			return false;
		return userRepository.existsByEmail(email);
	}

	public void saveUser(User user){
		String encoddedPassword = bCryptPasswordEncoder.encode(user.getPassword());
		user.setPassword(encoddedPassword);
		userRepository.saveAndFlush(user);
	}

	public Optional<User> getUserByEmail(String email){
		return userRepository.findByEmail(email);
	}

	public String getUserNameById(Integer id){
		Optional<User> u = userRepository.findById(id);
		if(u.isPresent()){
			User user = u.get();
			return user.getFullName();
		}
		throw new ResourceNotFoundException();
	}

	public User getAUthenticatedUser(){
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if(!getUserByEmail(auth.getName()).isPresent()) {
			throw new UsernameNotFoundException();
		}
		return getUserByEmail(auth.getName()).get();
	}
}