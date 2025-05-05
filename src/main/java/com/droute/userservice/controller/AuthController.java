package com.droute.userservice.controller;

import org.apache.coyote.BadRequestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.droute.userservice.DrouteUserServiceApplication;
import com.droute.userservice.dto.request.LoginUserRequestDto;
import com.droute.userservice.dto.request.RegisterUserRequestDto;
import com.droute.userservice.dto.request.ResetPasswordRequestDTO;
import com.droute.userservice.dto.response.CommonResponseDto;
import com.droute.userservice.dto.response.ResponseBuilder;
import com.droute.userservice.entity.UserEntity;
import com.droute.userservice.exception.EntityAlreadyExistsException;
import com.droute.userservice.service.EmailNotificationService;
import com.droute.userservice.service.UserEntityService;

import jakarta.persistence.EntityNotFoundException;

@RestController
@RequestMapping("/api/user/auth")
@CrossOrigin("*")
public class AuthController {

	@Autowired
	private UserEntityService userEntityService;

	@Autowired
	private EmailNotificationService emailNotificationService;

	private static final Logger logger = LoggerFactory.getLogger(DrouteUserServiceApplication.class);

	@PostMapping("/login")
	public ResponseEntity<CommonResponseDto<UserEntity>> loginUser(@RequestBody LoginUserRequestDto loginDetails)
			throws EntityNotFoundException, BadRequestException, IllegalArgumentException {
				System.out.println("user service login called");
		var isUserExist = userEntityService.checkUserExist(loginDetails);
		if (isUserExist != null) {
			return ResponseBuilder.success(HttpStatus.OK, "Logged in Successfully..", isUserExist);
		}
		return ResponseBuilder.failure(HttpStatus.BAD_REQUEST, "Wrong password entered.", "USR_400_WRONG_PASSWORD");

	}

	@PostMapping("/sendOTP")
	public ResponseEntity<CommonResponseDto<String>> sendOTPMail(@RequestBody String recipientEmail) {
		var otp = emailNotificationService.sendOtpNotification(recipientEmail);
		if (otp != null) {
			return ResponseBuilder.success(HttpStatus.OK,
					"Otp has been sent successfully to the email : " + recipientEmail, null);
		}
		return ResponseBuilder.failure(HttpStatus.BAD_REQUEST, "Failed to send OTP.", "USR_400_FAILED_TO_SEND_OTP");
	}

	@PostMapping("/signup")
	public ResponseEntity<CommonResponseDto<UserEntity>> createUser(@RequestBody RegisterUserRequestDto userDetails)
			throws EntityAlreadyExistsException, BadRequestException {
		logger.info("User sign-up called");
		var createdUser = userEntityService.signUpUser(userDetails);
		if (createdUser == null) {
			return ResponseBuilder.failure(HttpStatus.BAD_REQUEST, "Failed to create user.",
					"USR_400_FAILED_TO_CREATE_USER");
		}
		return ResponseBuilder.success(HttpStatus.CREATED, "User created successfully.", createdUser);

	}

	 @PutMapping("/reset-password")
    public ResponseEntity<CommonResponseDto<String>> updatePassword( @RequestBody ResetPasswordRequestDTO requestDTO) {
        userEntityService.updatePassword(requestDTO);

		return ResponseBuilder.success(HttpStatus.OK, "Password updated successfully.", null);
    }
	

}
