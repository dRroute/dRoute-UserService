package com.droute.userservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.droute.userservice.entity.Courier;

@Repository
public interface CourierRepository extends JpaRepository<Courier, Long>{
    
}
