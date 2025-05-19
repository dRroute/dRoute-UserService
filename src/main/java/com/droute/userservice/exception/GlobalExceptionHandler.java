package com.droute.userservice.exception;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.HashMap;
import java.util.Map;

import javax.mail.SendFailedException;

import org.apache.coyote.BadRequestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.droute.userservice.DrouteUserServiceApplication;
import com.droute.userservice.dto.response.CommonResponseDto;
import com.droute.userservice.dto.response.ResponseBuilder;
import com.droute.userservice.entity.UserEntity;

import jakarta.persistence.EntityNotFoundException;

@RestControllerAdvice
public class GlobalExceptionHandler {

	private static final Logger logger = LoggerFactory.getLogger(DrouteUserServiceApplication.class);

	@ExceptionHandler(EntityNotFoundException.class)
	public ResponseEntity<CommonResponseDto<UserEntity>> handleEntityNotFoundException(
			EntityNotFoundException exception) {

		logger.error(exception.getMessage());

		return ResponseBuilder.failure(HttpStatus.NOT_FOUND, exception.getMessage(), "USR_404_NOT_FOUND");

	}

	@ExceptionHandler(EntityAlreadyExistsException.class)
	public ResponseEntity<CommonResponseDto<UserEntity>> handleEntityAlreadyExistException(
			EntityAlreadyExistsException exception) {

		logger.error(exception.getMessage());

		return ResponseBuilder.failure(HttpStatus.CONFLICT, exception.getMessage(), "USR_409_CONFLICT");
	}

	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<CommonResponseDto<UserEntity>> handleIllegalArgumentException(
			IllegalArgumentException exception) {

		logger.error(exception.getMessage());

		return ResponseBuilder.failure(HttpStatus.BAD_REQUEST, exception.getMessage(), "USR_400_BAD_REQUEST");

	}

	@ExceptionHandler(BadRequestException.class)
	public ResponseEntity<CommonResponseDto<UserEntity>> handleBadRequestException(
			BadRequestException exception) {

		logger.error(exception.getMessage());

		return ResponseBuilder.failure(HttpStatus.BAD_REQUEST, exception.getMessage(), "USR_400_BAD_REQUEST");

	}

	@ExceptionHandler(SQLIntegrityConstraintViolationException.class)
	public ResponseEntity<CommonResponseDto<UserEntity>> handleSQLIntegrityConstraintViolationException(
			SQLIntegrityConstraintViolationException exception) {

		logger.error(exception.getMessage());

		return ResponseBuilder.failure(HttpStatus.CONFLICT, exception.getMessage(), "USR_409_CONFLICT");

	}

	// @Override
	// protected ResponseEntity handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {

	// 	Map<String, String> errors = new HashMap<>();
	// 	ex.getBindingResult().getFieldErrors()
	// 			.forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));

	// 	return ResponseBuilder.failure(HttpStatus.BAD_REQUEST, "Validation failed", errors);
	// }


	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<?> handleValidationErrors(MethodArgumentNotValidException ex) {
		Map<String, String> errors = new HashMap<>();
		ex.getBindingResult().getFieldErrors()
				.forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
		return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
	}
	@ExceptionHandler(SendFailedException.class)
	public ResponseEntity<?> handleValidationErrors(SendFailedException exception) {
		logger.error(exception.getMessage());

		return ResponseBuilder.failure(HttpStatus.BAD_REQUEST, exception.getMessage(), "USR_400_BAD_REQUEST");
	}
	@ExceptionHandler(com.sun.mail.smtp.SMTPAddressFailedException.class)
	public ResponseEntity<?> handleValidationErrors(com.sun.mail.smtp.SMTPAddressFailedException exception) {
		logger.error(exception.getMessage());

		return ResponseBuilder.failure(HttpStatus.BAD_REQUEST, exception.getMessage(), "USR_400_BAD_REQUEST");
	}

}
