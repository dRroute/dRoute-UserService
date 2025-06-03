package com.droute.userservice.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.droute.userservice.entity.UserEntity;


@Repository
public interface UserEntityRepository extends JpaRepository<UserEntity, Long> {
    
    // Find a user by email and password for login
    UserEntity findByEmailAndPassword(String email, String password);

    // Find a user by email to check if the user already exists
    Optional<UserEntity> findByEmail(String email);
    UserEntity getByEmailOrContactNo(String email, String contactNo);

	UserEntity findByContactNo(String contactNo);

    //check if user exists by email or contactNo
    boolean existsByEmailOrContactNo(String email, String contactNo);



	boolean existsByEmailAndPassword(String email, String password);
}

