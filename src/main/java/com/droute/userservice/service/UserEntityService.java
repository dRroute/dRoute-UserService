package com.droute.userservice.service;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.droute.userservice.dto.request.RegisterUserRequestDto;
import com.droute.userservice.entity.Role;
import com.droute.userservice.entity.UserEntity;
import com.droute.userservice.exception.EntityAlreadyExistsException;
import com.droute.userservice.repository.UserEntityRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class UserEntityService {

	@Autowired
	private UserEntityRepository userEntityRepository;

	// It is used by driver to register as a driver Role
	public UserEntity registerUser(RegisterUserRequestDto userDetails) throws EntityAlreadyExistsException {

		var user = userEntityRepository.findByEmail(userDetails.getEmail());
		
		// If user already exist with driver role then throw exception
		if (user != null && user.getRoles().contains(Role.DRIVER)) {
			throw new EntityAlreadyExistsException("User already exist with email = " + userDetails.getEmail());
		}

		// If user already exist with user role then add new role as driver
		else if (user != null && user.getRoles().contains(Role.USER)) {
			user.getRoles().add(Role.DRIVER);
			user.setContactNo(userDetails.getContactNo());
			user.setEmail(userDetails.getEmail());
			user.setFullName(userDetails.getFullName());
			user.setPassword(userDetails.getPassword());
			user.setColorHexValue(getRandomColor());
			return userEntityRepository.save(user);
		}

		// If user not exist then create new user with driver role
		Set<Role> roles = new HashSet<>();
		roles.add(Role.valueOf(userDetails.getRole().toUpperCase()));
		user = new UserEntity();
		user.setContactNo(userDetails.getContactNo());
		user.setEmail(userDetails.getEmail());
		user.setFullName(userDetails.getFullName());
		user.setPassword(userDetails.getPassword());
		user.setRoles(roles);
		user.setColorHexValue(getRandomColor());
	
		return userEntityRepository.save(user);

	}

	// It is used by user to register as a user Role
	public UserEntity signUpUser(RegisterUserRequestDto userDetails)
			throws EntityAlreadyExistsException, BadRequestException {
		if (!userDetails.getRole().equalsIgnoreCase("user")) {
			throw new BadRequestException("Role should be user");
		}
		var user = userEntityRepository.findByEmail(userDetails.getEmail());
		
		// If user already exist with driver role then throw exception
		if (user != null && user.getRoles().contains(Role.USER)) {
			throw new EntityAlreadyExistsException("User already exist with email = " + userDetails.getEmail());
		}

		// If user already exist with user role then add new role as driver
		else if (user != null && user.getRoles().contains(Role.DRIVER)) {
			user.getRoles().add(Role.USER);
			user.setContactNo(userDetails.getContactNo());
			user.setEmail(userDetails.getEmail());
			user.setFullName(userDetails.getFullName());
			user.setPassword(userDetails.getPassword());
			user.setColorHexValue(getRandomColor());
			return userEntityRepository.save(user);
		}

		// If user not exist then create new user with driver role
		Set<Role> roles = new HashSet<>();
		roles.add(Role.valueOf(userDetails.getRole().toUpperCase()));
		user = new UserEntity();
		user.setContactNo(userDetails.getContactNo());
		user.setEmail(userDetails.getEmail());
		user.setFullName(userDetails.getFullName());
		user.setPassword(userDetails.getPassword());
		user.setRoles(roles);
		user.setColorHexValue(getRandomColor());
	
		return userEntityRepository.save(user);

	}

	public String getRandomColor() {
		Random random = new Random();
		StringBuilder color = new StringBuilder("#");

		for (int i = 0; i < 6; i++) {
			// Restrict to darker shades by using lower hex values (0-8)
			int value = random.nextInt(8); // Generate a random number between 0 and 7
			color.append(Integer.toHexString(value));
		}

		return color.toString();
	}

	public UserEntity findUserById(Long userId) {

		return userEntityRepository.findById(userId).orElse(null);

	}

	public UserEntity updateUser(UserEntity user) {

		return userEntityRepository.save(user);

	}

	public void deleteUserById(Long userId) {
		var user = findUserById(userId);

		userEntityRepository.delete(user);

	}

	public UserEntity checkUserExist(String emailOrPhone, String password) {

		UserEntity userByEmail = userEntityRepository.findByEmail(emailOrPhone);
		UserEntity userByPhone = null;
		if (userByEmail == null) {
			userByPhone = userEntityRepository.findByContactNo(emailOrPhone);
		}

		if (userByEmail == null && userByPhone == null) {
			throw new EntityNotFoundException("User not found with mail or phone no. = " + emailOrPhone);
		} else if (userByPhone != null && userByPhone.getPassword().equals(password)) {
			return userByPhone;
		} else if (userByEmail != null && userByEmail.getPassword().equals(password)) {
			return userByEmail;
		}
		return null;

	}

}
