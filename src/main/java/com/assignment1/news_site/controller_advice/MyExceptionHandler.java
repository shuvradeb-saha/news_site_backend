package com.assignment1.news_site.controller_advice;

import com.assignment1.news_site.exception.AuthenticationCredException;
import com.assignment1.news_site.exception.ResourceNotFoundException;
import com.assignment1.news_site.exception.UsernameNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class MyExceptionHandler
{
	@ExceptionHandler({ResourceNotFoundException.class})
	public ResponseEntity handleResourceNotFound() {
		return new ResponseEntity<>("News You Requested Does Not Exist.",HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler({UsernameNotFoundException.class, NullPointerException.class})
	public ResponseEntity handleUserNotFound(){
		return new ResponseEntity<>("Email Not Found",HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(AuthenticationCredException.class)
	public ResponseEntity handleInvalidLoginCred(){
		return new ResponseEntity<>("Please Login",HttpStatus.BAD_REQUEST);
	}


}
