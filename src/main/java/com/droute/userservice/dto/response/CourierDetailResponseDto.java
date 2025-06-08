package com.droute.userservice.dto.response;

import java.time.LocalDateTime;

import com.droute.userservice.enums.CourierStatus;
import com.droute.userservice.enums.DimensionUnit;
import com.droute.userservice.enums.WeightUnit;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CourierDetailResponseDto {
    private Long courierId;
    private Long userId;
    private UserEntityResponseDto user;
    private String courierSourceAddress;
    private String courierSourceCoordinate;
    private String courierDestinationAddress;
    private String courierDestinationCoordinate;
    private Double courierHeight;
    private Double courierWidth;
    private Double courierLength;
    private DimensionUnit courierDimensionUnit;
    private Double courierWeight;
    private WeightUnit courierWeightUnit;
    private Double courierValue;
    private CourierStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}