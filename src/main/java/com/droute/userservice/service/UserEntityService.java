package com.droute.userservice.service;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import org.apache.coyote.BadRequestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.droute.userservice.DrouteUserServiceApplication;
import com.droute.userservice.dto.request.LoginUserRequestDto;
import com.droute.userservice.dto.request.RegisterUserRequestDto;
import com.droute.userservice.dto.request.ResetPasswordRequestDTO;
import com.droute.userservice.entity.UserEntity;
import com.droute.userservice.enums.Role;
import com.droute.userservice.exception.EntityAlreadyExistsException;
import com.droute.userservice.repository.UserEntityRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class UserEntityService {

	@Autowired
	private UserEntityRepository userEntityRepository;

	private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

	private static final Logger logger = LoggerFactory.getLogger(DrouteUserServiceApplication.class);

	// It is used by driver to register as a driver Role
	public UserEntity registerUser(RegisterUserRequestDto userDetails)
			throws BadRequestException, EntityAlreadyExistsException, DataIntegrityViolationException {

		var user = userEntityRepository.getByEmailOrContactNo(userDetails.getEmail(), userDetails.getContactNo());

		// If user already exist with driver role then throw exception
		if (user != null && user.getRoles().contains(Role.DRIVER)) {
			throw new EntityAlreadyExistsException("User already exist with either email = " + userDetails.getEmail() +
					" or contact no = " + userDetails.getContactNo());
		} else if (!userDetails.getPassword().equals(userDetails.getConfirmPassword())) {
			throw new BadRequestException("password and confirm password didn't match !");
		}

		// If user already exist with user role then add new role as driver
		else if (user != null && user.getRoles().contains(Role.USER)) {
			user.getRoles().add(Role.DRIVER);
			user.setContactNo(userDetails.getContactNo());
			user.setEmail(userDetails.getEmail());
			user.setFullName(userDetails.getFullName());
			user.setPassword(passwordEncoder.encode(userDetails.getPassword()));
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
		user.setPassword(passwordEncoder.encode(userDetails.getPassword()));
		user.setRoles(roles);
		user.setColorHexValue(getRandomColor());

		return userEntityRepository.save(user);

	}

	// It is used by user to register as a user Role
	public UserEntity signUpUser(RegisterUserRequestDto userDetails)
			throws EntityAlreadyExistsException, BadRequestException {
		logger.info("userDetails = " + userDetails);
		if (!userDetails.getRole().equalsIgnoreCase("user")) {
			logger.error("Role should be user");
			throw new BadRequestException("Role should be user");
		}
		var user = userEntityRepository.findByEmail(userDetails.getEmail()).orElseThrow(
				() -> new EntityNotFoundException("User not found with email = " + userDetails.getEmail()));

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
			user.setPassword(passwordEncoder.encode(userDetails.getPassword()));
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
		user.setPassword(passwordEncoder.encode(userDetails.getPassword()));
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

		return userEntityRepository.findById(userId)
				.orElseThrow(() -> new EntityNotFoundException("User not found with id = " + userId));

	}

	public UserEntity updateUser(UserEntity user) {

		return userEntityRepository.save(user);

	}

	public void deleteUserById(Long userId) {
		var user = findUserById(userId);

		userEntityRepository.delete(user);

	}

	public boolean checkUserExistById(Long userId) throws EntityNotFoundException {
		return userEntityRepository.existsById(userId);
	}

	public UserEntity checkUserExist(LoginUserRequestDto loginDetails)
			throws EntityNotFoundException, BadRequestException, IllegalArgumentException {

		UserEntity userByEmailOrPhone = userEntityRepository.getByEmailOrContactNo(loginDetails.getEmailOrPhone(),
				loginDetails.getEmailOrPhone());

		if (userByEmailOrPhone == null) {
			throw new EntityNotFoundException(
					"Email or Phone = " + loginDetails.getEmailOrPhone() + " doesn't exists. Please register first.");
		} else if (userByEmailOrPhone != null &&
				passwordEncoder.matches(loginDetails.getPassword(), userByEmailOrPhone.getPassword())

				&& userByEmailOrPhone.getRoles().contains(Role.valueOf(loginDetails.getRole().toUpperCase()))) {
			return userByEmailOrPhone;
		}
		else if (userByEmailOrPhone != null && !passwordEncoder.matches(loginDetails.getPassword(),
				userByEmailOrPhone.getPassword())) {
			throw new BadRequestException("Wrong password entered.");
		} else if (userByEmailOrPhone != null
				&& !userByEmailOrPhone.getRoles().contains(Role.valueOf(loginDetails.getRole().toUpperCase()))) {
			throw new IllegalArgumentException("Role not matched with user account.");
		}
		return null;

	}

	public void updatePassword(ResetPasswordRequestDTO requestDTO) {
		UserEntity user = userEntityRepository.findByEmail(requestDTO.getEmail())
				.orElseThrow(() -> new EntityNotFoundException("User not found with email = " + requestDTO.getEmail()));

		user.setPassword(passwordEncoder.encode(requestDTO.getNewPassword())); // set the encoded password
		userEntityRepository.save(user);
	}

}
