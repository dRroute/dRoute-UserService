package com.droute.userservice.dto.response;

import java.time.LocalDateTime;
import java.util.Set;
import com.droute.userservice.enums.Role;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserEntityResponseDto {
    private Long userId;
    private String fullName;
    private String email;
    private Set<Role> roles;
    private String contactNo;
    private String colorHexValue;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
