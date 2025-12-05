package com.bill.billing.service;

import com.bill.billing.client.service.booking_service.model.BookingClient;
import com.bill.billing.config.VnpayUrlBuilder;
import com.bill.billing.enums.PaymentStatus;
import com.bill.billing.enums.PaymentType;
import com.bill.billing.exception.BillingException;
import com.bill.billing.exception.BusinessException;
import com.bill.billing.exception.handler.VnpayCallbackHandler;
import com.bill.billing.model.entity.BasePayment;
import com.bill.billing.model.request.VNPAYRequest;
import com.bill.billing.model.response.ResponseData;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class VnpayService {

    private final VnpayUrlBuilder urlBuilder;
    private final VnpayCallbackHandler callbackHandler;
    private final BookingClient bookingClient;

    public ResponseData<String> createPaymentUrl(VNPAYRequest request, HttpServletRequest httpRequest) {
        BasePayment payment = callbackHandler.getPayment(request.getType(), request.getPaymentId());

        if (payment.getTotalAmount() == null || payment.getTotalAmount() <= 0) {
            throw new BusinessException(BillingException.INVALID_PAYMENT_AMOUNT);
        }

        try {
            String url = urlBuilder.buildPaymentUrl(
                payment.getId(),
                payment.getTotalAmount().longValue(),
                request.getType().name(),
                httpRequest
            );

            return new ResponseData<>(200, "Tạo URL thanh toán thành công", url);

        } catch (Exception e) {
            throw new BusinessException(BillingException.PAYMENT_GATEWAY_ERROR);
        }
    }

    public ResponseData<String> handleVnpayCallback(Map<String, String> params) {
        try {
            PaymentType type = PaymentType.valueOf(params.get("type"));
            Long paymentId = Long.valueOf(params.get("vnp_TxnRef"));

            BasePayment payment = callbackHandler.getPayment(type, paymentId);

            if (!callbackHandler.validateSignature(params)) {
                throw new BusinessException(BillingException.INVALID_SIGNATURE);
            }

            if ("00".equals(params.get("vnp_ResponseCode"))) {
                payment.setStatus(PaymentStatus.SUCCESS);
                payment.setPaymentTime(LocalDateTime.now());
                callbackHandler.savePayment(type, payment);

                bookingClient.updateBookingStatus(payment.getBookingId());

                return new ResponseData<>(200, "Thanh toán thành công", null);
            }

            payment.setStatus(PaymentStatus.FAILED);
            callbackHandler.savePayment(type, payment);

            return new ResponseData<>(400, "Thanh toán thất bại", null);

        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            throw new BusinessException(BillingException.PAYMENT_GATEWAY_ERROR);
        }
    }
}
