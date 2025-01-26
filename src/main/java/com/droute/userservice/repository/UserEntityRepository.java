package com.droute.userservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.droute.userservice.entity.UserEntity;


@Repository
public interface UserEntityRepository extends JpaRepository<UserEntity, Long> {
    
    // Find a user by email and password for login
    UserEntity findByEmailAndPassword(String email, String password);

    // Find a user by email to check if the user already exists
    UserEntity findByEmail(String email);

	UserEntity findByContactNo(String contactNo);
}

