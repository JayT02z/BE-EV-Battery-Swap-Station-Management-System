package com.bill.billing.model.entity;

import com.bill.billing.enums.PaymentMethod;
import com.bill.billing.enums.PaymentStatus;

import com.bill.billing.model.event.entity.Driver;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@MappedSuperclass
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public abstract class BasePayment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    private Driver customerId;

    private Double totalAmount;
    private Double baseAmount;
    private Double discountAmount;
    private Double taxAmount;

    @Enumerated(EnumType.STRING)
    private PaymentMethod method;

    @Enumerated(EnumType.STRING)
    private PaymentStatus status;

    private String description;
    private Long bookingId;

    private LocalDateTime paymentTime;
    private LocalDateTime createdAt = LocalDateTime.now();
}
