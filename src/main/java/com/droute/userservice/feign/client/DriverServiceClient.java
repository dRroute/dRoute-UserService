package com.droute.userservice.feign.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.droute.userservice.dto.response.CourierDetailResponseDto;

@Component
@FeignClient(name = "droute-cloud-gateway", path = "/api/driver")
public interface DriverServiceClient {

    @PostMapping("/journey-details/filter")
    public String getJourneysByCourierConditions(@RequestBody CourierDetailResponseDto courierDetail);

}
