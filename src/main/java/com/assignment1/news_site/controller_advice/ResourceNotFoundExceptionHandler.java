package com.assignment1.news_site.controller_advice;

import com.assignment1.news_site.exception.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class ResourceNotFoundExceptionHandler
{
	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity handleResourceNotFound() {
		return new ResponseEntity<>("News You Requested Does Not Exist.",HttpStatus.NOT_FOUND);
	}
}
