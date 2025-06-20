package com.droute.userservice.controller;

import java.util.List;

import javax.mail.SendFailedException;

import org.apache.coyote.BadRequestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.droute.userservice.dto.request.EmailNotificationRequestDto;
import com.droute.userservice.dto.request.RegisterUserRequestDto;
import com.droute.userservice.dto.response.CommonResponseDto;
import com.droute.userservice.dto.response.CourierDetailResponseDto;
import com.droute.userservice.dto.response.ResponseBuilder;
import com.droute.userservice.entity.UserEntity;
import com.droute.userservice.exception.EntityAlreadyExistsException;
import com.droute.userservice.service.EmailNotificationService;
import com.droute.userservice.service.UserEntityService;
import com.sun.mail.smtp.SMTPAddressFailedException;

import io.swagger.v3.oas.annotations.Hidden;
import jakarta.persistence.EntityNotFoundException;


@RestController
@RequestMapping("/api/user")
@CrossOrigin("*")
public class UserEntityController {

	private Logger logger = LoggerFactory.getLogger(UserEntityController.class);
	@Autowired
	private UserEntityService userEntityService;

	@Autowired
	private EmailNotificationService emailNotificationService;

	@Hidden
	@PostMapping("/")
	public ResponseEntity<CommonResponseDto<UserEntity>> createUser(@RequestBody RegisterUserRequestDto userDetails)
			throws EntityAlreadyExistsException, BadRequestException  {
		logger.info("user register api called");
		if (userDetails.getRole().equalsIgnoreCase("driver")) {
			var createdUser = userEntityService.registerUser(userDetails);
			return ResponseBuilder.success(HttpStatus.OK, "Driver Created Successfully...", createdUser);
		}
		return ResponseBuilder.failure(HttpStatus.BAD_REQUEST, "Invalid role given", "USR_400_INVALID_ROLE");

	}

	@GetMapping("/{userId}")
	public ResponseEntity<CommonResponseDto<UserEntity>> getUserById(@PathVariable Long userId) {
		var user = userEntityService.findUserById(userId);
		return ResponseBuilder.success(HttpStatus.OK, "User founded successfully.", user);
	}

	@GetMapping("")
	public ResponseEntity<CommonResponseDto<List<UserEntity>>> getAllUser() {
		var users = userEntityService.getAllUser();
		if (users == null || users.isEmpty() ) {
			throw new EntityNotFoundException("No user details are present at this moment.");
		}
		return ResponseBuilder.success(HttpStatus.OK, "User founded successfully.", users);
	}

	@GetMapping("/exist/{userId}")
	public ResponseEntity<CommonResponseDto<Boolean>> checkUserExistById(@PathVariable Long userId) {
		var result = userEntityService.checkUserExistById(userId);
		if(!result) {
			return ResponseBuilder.failure(HttpStatus.NOT_FOUND, "User not found", "USR_404_NOT_FOUND");
		}
		return ResponseBuilder.success(HttpStatus.OK, "User founded successfully.", result);
	}
	


	@PutMapping("/{userId}")
	public ResponseEntity<CommonResponseDto<UserEntity>> updateUserById(@RequestBody UserEntity user, @PathVariable Long userId) {
		if(user == null) return ResponseBuilder.failure(HttpStatus.BAD_REQUEST, "Request body cannot be null", "USR_400_BAD_REQUEST");
		var updatedUser = userEntityService.updateUser(user, userId);
	
		return ResponseBuilder.success(HttpStatus.OK, "User Updated Successfully...", updatedUser);

	}

	@DeleteMapping("/{userId}")
	public ResponseEntity<CommonResponseDto<UserEntity>> deleteUserById(@PathVariable Long userId) {
		userEntityService.deleteUserById(userId);
		return ResponseBuilder.success(HttpStatus.OK, "User Deleted Successfully...", null);

	}

	@GetMapping("/{userId}/couriers")
    public ResponseEntity<CommonResponseDto<List<CourierDetailResponseDto>>> getCourierByUserId(@PathVariable Long userId) {
        var data = userEntityService.getAllCourierByUserId(userId);
        if (data == null || data.isEmpty()) {
            throw new EntityNotFoundException("No courier found at the moment..");
            
        } 
        return ResponseBuilder.success(HttpStatus.OK, "Courier details founded successfully", data);
    }

	@PostMapping("/custom-email-notification")
	public ResponseEntity<CommonResponseDto<Object>> sendCustomEmailNotification(@RequestBody EmailNotificationRequestDto request) throws SMTPAddressFailedException, SendFailedException {
		emailNotificationService.sendCustomEmailNotification(request);

		return ResponseBuilder.success(HttpStatus.OK, "Email sent successfully to "+request.getReceiverEmail(), null);
	}
	
	

}
