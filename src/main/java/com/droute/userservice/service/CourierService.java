package com.droute.userservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.droute.userservice.dto.request.CourierDetailsRequestDto;
import com.droute.userservice.dto.response.CourierDetailResponseDto;
import com.droute.userservice.entity.Courier;
import com.droute.userservice.entity.UserEntity;
import com.droute.userservice.enums.DimensionUnit;
import com.droute.userservice.enums.WeightUnit;
import com.droute.userservice.repository.CourierRepository;
import com.droute.userservice.repository.UserEntityRepository;

import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CourierService {

    @Autowired
    private CourierRepository courierRepository;

    @Autowired
    private UserEntityRepository userEntityRepository;

    // Create
    
    public CourierDetailResponseDto createCourier(CourierDetailsRequestDto courierDetails) {
        System.out.println("user id in create = "+courierDetails.getUserId());
        Courier courier = toEntity(courierDetails);
        Courier savedCourier = courierRepository.save(courier);
        return toResponse(savedCourier);
    }

    // Read (Single)
    public CourierDetailResponseDto getCourierById(long courierId) {
        Courier courier = courierRepository.findById(courierId)
                .orElseThrow(() -> new EntityNotFoundException("Courier not found with id = " + courierId));
        return toResponse(courier);
    }

    // Check if Courier exists
    public boolean courierExistsById(long courierId) {
        return courierRepository.existsById(courierId);
    }

    // Read (All)
    public List<CourierDetailResponseDto> getAllCouriers() {
        return courierRepository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    // Update
    @Transactional
    public CourierDetailResponseDto updateCourier(long courierId, CourierDetailsRequestDto courierDetails) {
        Courier existingCourier = courierRepository.findById(courierId)
                .orElseThrow(() -> new EntityNotFoundException("Courier not found with id = " + courierId));

        // Update fields
        existingCourier.setCourierSourceAddress(courierDetails.getCourierSourceAddress());
        existingCourier.setCourierSourceCoordinate(courierDetails.getCourierSourceCoordinate());
        existingCourier.setCourierDestinationAddress(courierDetails.getCourierDestinationAddress());
        existingCourier.setCourierDestinationCoordinate(courierDetails.getCourierDestinationCoordinate());
        existingCourier.setCourierHeight(courierDetails.getCourierHeight());
        existingCourier.setCourierWidth(courierDetails.getCourierWidth());
        existingCourier.setCourierLength(courierDetails.getCourierLength());
        existingCourier.setCourierDimensionUnit(DimensionUnit.fromAbbreviation(courierDetails.getCourierDimensionUnit().toLowerCase()));
        existingCourier.setCourierWeight(courierDetails.getCourierWeight());
        existingCourier.setCourierWeightUnit(WeightUnit.fromAbbreviation((courierDetails.getCourierWeightUnit().toLowerCase())));
        existingCourier.setCourierValue(courierDetails.getCourierValue());

        // Only update user if different
        if (!existingCourier.getUser().getUserId().equals(courierDetails.getUserId())) {
            var user = userEntityRepository.findById(courierDetails.getUserId())
                    .orElseThrow(() -> new EntityNotFoundException("User not found with id = " + courierDetails.getUserId()));
            existingCourier.setUser(user);
        }

        Courier updatedCourier = courierRepository.save(existingCourier);
        return toResponse(updatedCourier);
    }

    // Delete
    @Transactional
    public void deleteCourier(long courierId) {
        if (!courierRepository.existsById(courierId)) {
            throw new EntityNotFoundException("Courier not found with id = " + courierId);
        }
        courierRepository.deleteById(courierId);
    }

    // Entity to DTO conversion
    private CourierDetailResponseDto toResponse(Courier courier) {
        return CourierDetailResponseDto.builder()
                .courierId(courier.getCourierId())
                .userId(courier.getUser().getUserId())
                .courierSourceAddress(courier.getCourierSourceAddress())
                .courierSourceCoordinate(courier.getCourierSourceCoordinate())
                .courierDestinationAddress(courier.getCourierDestinationAddress())
                .courierDestinationCoordinate(courier.getCourierDestinationCoordinate())
                .courierHeight(courier.getCourierHeight())
                .courierWidth(courier.getCourierWidth())
                .courierLength(courier.getCourierLength())
                .courierDimensionUnit(courier.getCourierDimensionUnit())
                .courierWeight(courier.getCourierWeight())
                .courierWeightUnit(courier.getCourierWeightUnit())
                .courierValue(courier.getCourierValue())
                .createdAt(courier.getCreatedAt())
                .updatedAt(courier.getUpdatedAt())
                .build();
    }

    private Courier toEntity(CourierDetailsRequestDto request) {
    // 1. Validate input
    if (request == null) {
        throw new IllegalArgumentException("Request cannot be null");
    }
    
    // 2. Debug logging
    System.out.println("Converting DTO to Entity for user ID: " + request.getUserId());
    
    // 3. Find user with null check
    UserEntity user = Optional.ofNullable(request.getUserId())
        .flatMap(userId -> userEntityRepository.findById(userId))
        .orElseThrow(() -> new EntityNotFoundException(
            "User not found with id: " + request.getUserId()));
    
    // 4. Build entity with null checks
    return Courier.builder()
        .user(user)
        .courierSourceAddress(request.getCourierSourceAddress())
        .courierSourceCoordinate(request.getCourierSourceCoordinate())
        .courierDestinationAddress(request.getCourierDestinationAddress())
        .courierDestinationCoordinate(request.getCourierDestinationCoordinate())
        .courierHeight(request.getCourierHeight())
        .courierWidth(request.getCourierWidth())
        .courierLength(request.getCourierLength())
        .courierDimensionUnit(DimensionUnit.fromAbbreviation(request.getCourierDimensionUnit()))
        .courierWeight(request.getCourierWeight())
        .courierWeightUnit(WeightUnit.fromAbbreviation(request.getCourierWeightUnit()))
        .courierValue(request.getCourierValue())
        .build();
}

    public UserEntity getUserByCourierId(Long courierId) {
        
        return courierRepository.findById(courierId)
                .orElseThrow(() -> new EntityNotFoundException("Courier not found with id = " + courierId))
                .getUser();
    }
}