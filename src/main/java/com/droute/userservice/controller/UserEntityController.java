package com.droute.userservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.droute.userservice.dto.CommonResponseDto;
import com.droute.userservice.dto.RegisterUserRequestDto;
import com.droute.userservice.entity.UserEntity;
import com.droute.userservice.exception.EntityAlreadyExistsException;
import com.droute.userservice.service.UserEntityService;

@RestController
@RequestMapping("/api/user")
public class UserEntityController {

	@Autowired
	private UserEntityService userEntityService;

	@PostMapping("/")
	public ResponseEntity<CommonResponseDto<UserEntity>> createUser(@RequestBody RegisterUserRequestDto userDetails) throws EntityAlreadyExistsException {
		
      if(userDetails.getRole().equalsIgnoreCase("user")) {
    	  var createdUser = userEntityService.registerUser(userDetails);
    	  var crd = new CommonResponseDto<UserEntity>("User created Successfully.", createdUser);

  		return ResponseEntity.status(HttpStatus.CREATED).body(crd);

      }
  	return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new CommonResponseDto<>("Invalid role given",null));
	
	}

	@GetMapping("/{userId}")
	public ResponseEntity<CommonResponseDto<UserEntity>> getUserById(@PathVariable Long userId) {
		var user = userEntityService.findUserById(userId);

		var crd = new CommonResponseDto<UserEntity>("User found Successfully.", user);

		return ResponseEntity.status(HttpStatus.OK).body(crd);

	}
	
	@PutMapping("/")
	public ResponseEntity<CommonResponseDto<UserEntity>> updateUserById(@RequestBody UserEntity user) {
		var updatedUser = userEntityService.updateUser(user);
		var crd = new CommonResponseDto<UserEntity>("User updated Successfully.", updatedUser);

		return ResponseEntity.status(HttpStatus.CREATED).body(crd);
		
	}
	
	@DeleteMapping("/{userId}")
	public ResponseEntity<CommonResponseDto<UserEntity>> deleteUserById(@PathVariable Long userId) {
		 userEntityService.deleteUserById(userId);
		var crd = new CommonResponseDto<UserEntity>("User deleted Successfully.", null);

		return ResponseEntity.status(HttpStatus.OK).body(crd);
		
	}
	
	
	

}
