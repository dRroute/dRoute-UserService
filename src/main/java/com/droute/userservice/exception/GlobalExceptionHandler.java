package com.droute.userservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import jakarta.persistence.EntityNotFoundException;

@ControllerAdvice
@ResponseStatus
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
	
	@ExceptionHandler(EntityNotFoundException.class)
	public ResponseEntity<ErrorMessage> entityNotFoundException(EntityNotFoundException exception) {

		var errorMessage = new ErrorMessage(HttpStatus.NOT_FOUND, exception.getMessage());

		logger.error(exception.getMessage());
 
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorMessage);

	}
	@ExceptionHandler(EntityAlreadyExistsException.class)
	public ResponseEntity<ErrorMessage> entityNotFoundException(EntityAlreadyExistsException exception) {
		
		var errorMessage = new ErrorMessage(HttpStatus.NOT_FOUND, exception.getMessage());
		
		logger.error(exception.getMessage());
		
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorMessage);
		
	}
 
}
