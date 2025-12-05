package com.bill.billing.model.DTO;

import com.bill.billing.enums.PaymentMethod;
import com.bill.billing.enums.PaymentStatus;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PackagePaymentDTO {
    private Long id;
    private DriverDTO customerId;
    private Double totalAmount;
    private Double baseAmount;
    private Double discountAmount;
    private Double taxAmount;
    private PaymentMethod method;
    private PaymentStatus status;
    private Long bookingId;
    private String description;
    private LocalDateTime paymentTime;
    private LocalDateTime createdAt;

    private Long packageId;
    private LocalDate startDate;
    private LocalDate endDate;


}
