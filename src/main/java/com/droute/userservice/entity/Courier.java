package com.droute.userservice.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.droute.userservice.enums.CourierStatus;
import com.droute.userservice.enums.DimensionUnit;
import com.droute.userservice.enums.WeightUnit;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "couriers")
@Builder
public class Courier {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long courierId;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;
    
    private String courierSourceAddress;
    private String courierSourceCoordinate;
    private String courierDestinationAddress;
    private String courierDestinationCoordinate;
    
    private Double courierHeight;
    private Double courierWidth;
    private Double courierLength;
    
    @Enumerated(EnumType.STRING)
    private DimensionUnit courierDimensionUnit;
    
    private Double courierWeight;
    
    @Enumerated(EnumType.STRING)
    private WeightUnit courierWeightUnit;
    
    private Double courierValue;

    @Enumerated(EnumType.STRING)
    private CourierStatus status;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}