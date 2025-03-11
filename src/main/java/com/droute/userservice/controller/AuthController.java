package com.droute.userservice.controller;

import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.droute.userservice.dto.request.RegisterUserRequestDto;
import com.droute.userservice.dto.response.CommonResponseDto;
import com.droute.userservice.entity.UserEntity;
import com.droute.userservice.exception.EntityAlreadyExistsException;
import com.droute.userservice.service.EmailNotificationService;
import com.droute.userservice.service.UserEntityService;

@RestController
@RequestMapping("/api/user/auth")
@CrossOrigin("*")
public class AuthController {

	@Autowired
	private UserEntityService userEntityService;

	@Autowired
	private EmailNotificationService emailNotificationService;

	@GetMapping("/login")
	public ResponseEntity<CommonResponseDto<UserEntity>> loginUser(@RequestParam String emailOrPhone,
			@RequestParam String password) {
		var isUserExist = userEntityService.checkUserExist(emailOrPhone, password);
		if (isUserExist !=null) {
			return ResponseEntity.status(HttpStatus.OK)
					.body(new CommonResponseDto<UserEntity>("Logged in Successfully..", isUserExist));
		}
		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
				.body(new CommonResponseDto<UserEntity>("Wrong password entered.", null));

	}

	@PostMapping("/sendOTP")
	public ResponseEntity<String> sendOTPMail(@RequestParam("email") String recipientEmail) {
		var otp = emailNotificationService.sendOtpNotification(recipientEmail);
		if (otp != null) {
			return ResponseEntity.ok(otp);
		}
		return ResponseEntity.badRequest().body(null);
	}

	@PostMapping("/signup")
	public ResponseEntity<CommonResponseDto<UserEntity>> createUser(@RequestBody RegisterUserRequestDto userDetails) throws EntityAlreadyExistsException, BadRequestException {
		var createdUser = userEntityService.signUpUser(userDetails);

		var crd = new CommonResponseDto<UserEntity>("User created Successfully.", createdUser);

		return ResponseEntity.status(HttpStatus.CREATED).body(crd);

	}

}
