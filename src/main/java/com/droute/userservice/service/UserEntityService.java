package com.droute.userservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.droute.userservice.entity.UserEntity;
import com.droute.userservice.repository.UserEntityRepository;

@Service
public class UserEntityService {
	
	@Autowired
	private UserEntityRepository userEntityRepository;
	
	public UserEntity registerUser(UserEntity user ) {
		
		return userEntityRepository.save(user);
		
	}
	public UserEntity findUserById(Long userId ) {
		
		return userEntityRepository.findById(userId).orElse(null);
		
	}
	public UserEntity updateUser(UserEntity user ) {
		
		return userEntityRepository.save(user);
		
	}
	public void deleteUserById(Long userId ) {
		var user = findUserById(userId);
		
		 userEntityRepository.delete(user);
		
	}
	
	

}
