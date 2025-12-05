package com.bill.billing.model.request;

import com.bill.billing.enums.PaymentMethod;
import com.bill.billing.enums.PaymentStatus;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SingleSwapPaymentRequest {
    private String customerId;
    private Double totalAmount;
    private Double baseAmount;
    private Double discountAmount;
    private Double taxAmount;
    private PaymentMethod method;
    private PaymentStatus status;
    private String transactionId;
    private String description;
    private LocalDateTime paymentTime;
    private Long bookingId;
    private String stationId;
    private Long swapLogId;
    private Long packageId;
}
