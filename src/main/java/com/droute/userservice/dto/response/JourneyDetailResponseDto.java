package com.droute.userservice.dto.response;

import java.time.LocalDateTime;

import com.droute.userservice.enums.DimensionUnit;
import com.droute.userservice.enums.WeightUnit;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class JourneyDetailResponseDto {
    private Long journeyId;
    private Long driverId;
    private LocationDetailsResponseDto journeySource;
    private LocationDetailsResponseDto journeyDestination;
    private String visitedStateDuringJourney;
    private Double availableLength;
    private Double availableWidth;
    private Double availableHeight;
    private DimensionUnit availableSpaceMeasurementType;
    private Double availableWeight;
    private WeightUnit availableWeightMeasurementType;
    private JourneyStatus status;
    private Long totalConfirmedPackages;
    private LocalDateTime expectedDepartureDateTime;
    private LocalDateTime expectedArrivalDateTime;
}