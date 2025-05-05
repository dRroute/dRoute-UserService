package com.droute.userservice.feign.error.decoder;

import java.io.IOException;

import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import com.droute.userservice.dto.response.CommonResponseDto;
import com.droute.userservice.exception.DriverServiceException;
import com.droute.userservice.exception.EntityAlreadyExistsException;
import com.fasterxml.jackson.databind.ObjectMapper;

import feign.Response;
import feign.Util;
import feign.codec.ErrorDecoder;
import jakarta.persistence.EntityNotFoundException;

public class CustomFeignErrorDecoder implements ErrorDecoder {


    @Override
    public Exception decode(String methodKey, Response response) {
        HttpStatus status = HttpStatus.valueOf(response.status());
        String message = "Unknown error";
        try {
            // Deserialize the response body into CommonResponseDto
            String responseBody = Util.toString(response.body().asReader());
            ObjectMapper objectMapper = new ObjectMapper();
            CommonResponseDto<?> commonResponseDto = objectMapper.readValue(responseBody, CommonResponseDto.class);
            
            // Extract the message field
            message = commonResponseDto.getMessage();
            System.out.println("message = " + message);
        } catch (IOException ignored) {
            // Log or handle the exception if needed
        }

        switch (status) {
            case BAD_REQUEST:
                return new BadRequestException(message);
            case NOT_FOUND:
                return new EntityNotFoundException(message);
            case CONFLICT:
                return new EntityAlreadyExistsException(message);
            case FOUND:
                return new EntityAlreadyExistsException(message);
            default:
                return new DriverServiceException(message);
        }
    }
}
