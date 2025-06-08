package com.droute.userservice.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.droute.userservice.entity.Courier;

@Repository
public interface CourierRepository extends JpaRepository<Courier, Long>{

    List<Courier> findAllByUser_UserId(Long userId);
    
}
