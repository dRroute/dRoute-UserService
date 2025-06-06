package com.droute.userservice.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FilteredJourneyDetailsResponseDto {

    private CompleteDriverDetailsResponseDto driver;
    private JourneyDetailResponseDto journey;
    private Float averageDriverRating;
     
}
