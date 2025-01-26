package com.droute.userservice.service;

import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.droute.userservice.dto.RegisterUserRequestDto;
import com.droute.userservice.entity.UserEntity;
import com.droute.userservice.exception.EntityAlreadyExistsException;
import com.droute.userservice.repository.UserEntityRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class UserEntityService {

	@Autowired
	private UserEntityRepository userEntityRepository;

	public UserEntity registerUser(RegisterUserRequestDto userDetails) throws EntityAlreadyExistsException {
		
		var user = userEntityRepository.findByEmail(userDetails.getEmail());
		if(user != null) {
			throw new EntityAlreadyExistsException("User already exist with email = "+userDetails.getEmail());
		}
		user = new UserEntity(userDetails.getFullName(),userDetails.getEmail(), userDetails.getPassword(),userDetails.getRole(),userDetails.getContactNo());
		user.setColorHexValue(getRandomColor());
		return userEntityRepository.save(user);

	}
	
	 public  String getRandomColor() {
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
		if(userByEmail==null) {
			 userByPhone = userEntityRepository.findByContactNo(emailOrPhone);
		}

		if (userByEmail == null && userByPhone == null) {
			throw new EntityNotFoundException("User not found with mail or phone no. = " + emailOrPhone);
		}
		else if(userByPhone != null && userByPhone.getPassword().equals(password)) {
			return userByPhone;
		}
		else if (userByEmail != null && userByEmail.getPassword().equals(password)) {
			return userByEmail;
		}
		return null;

	}

}
