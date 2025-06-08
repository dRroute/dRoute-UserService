package com.droute.userservice.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.droute.userservice.dto.request.CourierDetailsRequestDto;
import com.droute.userservice.dto.response.CommonResponseDto;
import com.droute.userservice.dto.response.CourierDetailResponseDto;
import com.droute.userservice.dto.response.FilteredJourneyDetailsResponseDto;
import com.droute.userservice.dto.response.ResponseBuilder;
import com.droute.userservice.feign.client.DriverServiceClient;
import com.droute.userservice.service.CourierService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/user/courier")
@CrossOrigin("*")
public class CourierController {

    @Autowired
    private CourierService courierService;

    @Autowired
    private DriverServiceClient driverServiceClient;

    private static final Logger logger = LoggerFactory.getLogger(CourierController.class);

    // API to get jouney by courier
    @GetMapping("/{courierId}/journeys")
    public ResponseEntity<CommonResponseDto<List<FilteredJourneyDetailsResponseDto>>> getJourneysBasedOnCourier(
            @PathVariable Long courierId) {
        // Fetch courier details from DB
        var courierDetail = courierService.getCourierById(courierId);

        // Call Driver Service via RestTemplate or WebClient
        var response = driverServiceClient.getJourneysByCourierConditions(courierDetail);

        return ResponseBuilder.success(HttpStatus.OK, response.getMessage(), response.getData());
    }

    @PostMapping("")
    public ResponseEntity<CommonResponseDto<CourierDetailResponseDto>> postCourier(
            @Valid @RequestBody CourierDetailsRequestDto courierDetails) {
        logger.info("Received payload - userId: {}", courierDetails);

        var data = courierService.createCourier(courierDetails);

        return ResponseBuilder.success(HttpStatus.CREATED, "Courier details added successfully", data);
    }

    @GetMapping("/{courierId}")
    public ResponseEntity<CommonResponseDto<CourierDetailResponseDto>> getCourierById(@PathVariable Long courierId) {
        var data = courierService.getCourierById(courierId);
        System.out.println("courier detail = " + data);
        return ResponseBuilder.success(HttpStatus.OK, "Courier details founded successfully", data);
    }

    @GetMapping("/{courierId}/exists")
    public ResponseEntity<CommonResponseDto<Boolean>> courierExistsById(@PathVariable Long courierId) {
        logger.info("checking existance of courierId {}", courierId);
        boolean exists = courierService.courierExistsById(courierId);
        return ResponseBuilder.success(HttpStatus.OK, "Courier existence checked successfully", exists);
    }

    @PutMapping("/{courierId}")
    public ResponseEntity<CommonResponseDto<CourierDetailResponseDto>> postCourier(
            @RequestBody CourierDetailsRequestDto courierDetails, @PathVariable Long courierId) {

                System.out.println("Update coueir called with id = "+ courierId);
        var data = courierService.updateCourier(courierId, courierDetails);

        return ResponseBuilder.success(HttpStatus.OK, "Courier details updated successfully", data);
    }

    @DeleteMapping("/{courierId}")
    public ResponseEntity<CommonResponseDto<CourierDetailResponseDto>> deleteCourierById(@PathVariable Long courierId) {
        courierService.deleteCourier(courierId);
        return ResponseBuilder.success(HttpStatus.OK, "Courier details founded successfully", null);
    }

}
