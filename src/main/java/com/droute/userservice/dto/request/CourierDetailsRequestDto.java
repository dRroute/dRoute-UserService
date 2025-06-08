package com.droute.userservice.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class CourierDetailsRequestDto {
    // @NotNull(message = "User ID is required")
    private Long userId;
    
    // @NotBlank(message = "Source address is required")
    // @NotNull(message = "courierSourceAddress is required")
    private String courierSourceAddress;
    
    // @NotBlank(message = "Source coordinates are required")
    // @NotNull(message = "courierSourceCoordinate is required")
    private String courierSourceCoordinate;
    
    // @NotBlank(message = "Destination address is required")
    // @NotNull(message = "courierDestinationAddress is required")
    private String courierDestinationAddress;
    
    // @NotBlank(message = "Destination coordinates are required")
    private String courierDestinationCoordinate;
    
    // @Positive(message = "Height must be positive")
    private Double courierHeight;
    
    // @Positive(message = "Width must be positive")
    private Double courierWidth;
    
    // @Positive(message = "Length must be positive")
    private Double courierLength;
    
    // @NotNull(message = "Dimension unit is required")
    private String courierDimensionUnit;
    
    // @Positive(message = "Weight must be positive")
    private Double courierWeight;
    
    // @NotNull(message = "Weight unit is required")
    private String courierWeightUnit;
    
    // @PositiveOrZero(message = "Value must be positive or zero")
    private Double courierValue;

    // @NotNull(message = "status is required")
    private String status;

    
}