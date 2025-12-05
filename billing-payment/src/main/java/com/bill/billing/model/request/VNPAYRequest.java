package com.bill.billing.model.request;

import com.bill.billing.enums.PaymentType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Request object cho VNPAY
 * Dùng cho tạo URL thanh toán và callback
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class VNPAYRequest {

    // ID của bản ghi Payment nội bộ
    private Long paymentId;

    // Loại Payment: "single" hoặc "package"
    private PaymentType type;

}
