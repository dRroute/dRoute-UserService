package com.droute.userservice.exception;

import org.apache.coyote.BadRequestException;
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

		var errorMessage = new ErrorMessage(HttpStatus.BAD_REQUEST, exception.getMessage());

		logger.error(exception.getMessage());
 
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);

	}
	@ExceptionHandler(EntityAlreadyExistsException.class)
	public ResponseEntity<ErrorMessage> entityAlreadyExistException(EntityAlreadyExistsException exception) {
		
		var errorMessage = new ErrorMessage(HttpStatus.BAD_REQUEST, exception.getMessage());
		
		logger.error(exception.getMessage());
		
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
		
	}
 
	@ExceptionHandler(BadRequestException.class)
	public ResponseEntity<ErrorMessage> handleBadRequestException(BadRequestException exception) {
		
		var errorMessage = new ErrorMessage(HttpStatus.BAD_REQUEST, exception.getMessage());
		
		logger.error(exception.getMessage());
		
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
		
	}
 
}
