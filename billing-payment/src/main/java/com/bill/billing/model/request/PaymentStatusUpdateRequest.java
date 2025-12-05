package com.bill.billing.model.request;

import com.bill.billing.enums.PaymentStatus;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentStatusUpdateRequest {
    private Long paymentId;
    private PaymentStatus newStatus;
}
