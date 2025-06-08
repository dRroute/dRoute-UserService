package com.droute.userservice.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class LocationDetailsResponseDto {

    private Long locationId;

    private String longitude;
    private String latitude;
    
    private String address;
    
    private String city;
    private String pinCode;
    private String state;
    private String country;

    
   
}
