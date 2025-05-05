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
	public UserEntity registerUser(RegisterUserRequestDto userDetails) throws BadRequestException, EntityAlreadyExistsException, DataIntegrityViolationException {

		var user = userEntityRepository.findByEmail(userDetails.getEmail()).orElseThrow(() -> new EntityNotFoundException("User not found with email = " + userDetails.getEmail()));

		// If user already exist with driver role then throw exception
		if (user != null && user.getRoles().contains(Role.DRIVER)) {
			throw new EntityAlreadyExistsException("User already exist with email = " + userDetails.getEmail());
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
		var user = userEntityRepository.findByEmail(userDetails.getEmail()).orElseThrow(() -> new EntityNotFoundException("User not found with email = " + userDetails.getEmail()));

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

		return userEntityRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException("User not found with id = " + userId));

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

		UserEntity userByEmail = userEntityRepository.findByEmail(loginDetails.getEmailOrPhone()).get();
		UserEntity userByPhone = null;
		if (userByEmail == null) {
			userByPhone = userEntityRepository.findByContactNo(loginDetails.getEmailOrPhone());
		}

		if (userByEmail == null && userByPhone == null) {
			throw new EntityNotFoundException(
					"User not found with mail or phone no. = " + loginDetails.getEmailOrPhone());
		} else if (userByPhone != null &&
		passwordEncoder.matches(loginDetails.getPassword(), userByPhone.getPassword())

				&& userByPhone.getRoles().contains(Role.valueOf(loginDetails.getRole().toUpperCase()))) {
			return userByPhone;
		} else if (userByEmail != null && passwordEncoder.matches(loginDetails.getPassword(), userByEmail.getPassword())
				&& userByEmail.getRoles().contains(Role.valueOf(loginDetails.getRole().toUpperCase()))) {
			return userByEmail;
		}
		return null;

	}


	 public void updatePassword(ResetPasswordRequestDTO requestDTO) {
        UserEntity user = userEntityRepository.findByEmail(requestDTO.getEmail())
                .orElseThrow(() -> new EntityNotFoundException("User not found with email = " + requestDTO.getEmail()));

        user.setPassword(passwordEncoder.encode(requestDTO.getNewPassword()));  // set the encoded password
        userEntityRepository.save(user);
    }
	

}
