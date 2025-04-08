package com.droute.userservice.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class RegisterUserRequestDto {
	
	private String fullName;
	private String email;
	private String password;
	private String confirmPassword;
	private String contactNo;
	private String role;
	
	
}
