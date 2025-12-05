package com.bill.billing.model.DTO;

import com.bill.billing.enums.PaymentMethod;
import com.bill.billing.enums.PaymentStatus;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SingleSwapPaymentDTO {

    private Long id;
    private DriverDTO customerId;
    private Double totalAmount;
    private Double baseAmount;
    private Double discountAmount;
    private Double taxAmount;
    private PaymentMethod method;
    private PaymentStatus status;
    private String description;
    private Long bookingId;
    private Long packageId;
    private LocalDateTime paymentTime;
    private LocalDateTime createdAt;
    private String stationId;
}
