package com.droute.userservice.exception;

import java.sql.SQLIntegrityConstraintViolationException;

import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.droute.userservice.dto.response.CommonResponseDto;
import com.droute.userservice.dto.response.ResponseBuilder;
import com.droute.userservice.entity.UserEntity;

import jakarta.persistence.EntityNotFoundException;

@ControllerAdvice
@ResponseStatus
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

	@ExceptionHandler(EntityNotFoundException.class)
	public ResponseEntity<CommonResponseDto<UserEntity>> handleEntityNotFoundException(EntityNotFoundException exception) {

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

}
