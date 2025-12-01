package com.bill.billing.exception.handler;

import com.bill.billing.config.VnpayConfigProperties;
import com.bill.billing.enums.PaymentType;
import com.bill.billing.exception.BillingException;
import com.bill.billing.exception.BusinessException;
import com.bill.billing.model.entity.BasePayment;
import com.bill.billing.model.entity.PackagePayment;
import com.bill.billing.model.entity.SingleSwapPayment;
import com.bill.billing.repository.PackagePaymentRepository;
import com.bill.billing.repository.SingleSwapPaymentRepository;
import com.bill.billing.utils.VnpaySignatureUtil;
import java.util.*;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class VnpayCallbackHandler {
  private final VnpayConfigProperties config;

  private final PackagePaymentRepository packageRepo;
  private final SingleSwapPaymentRepository singleRepo;

  public BasePayment getPayment(PaymentType type, Long id) {
    return switch (type) {
      case SINGLE -> singleRepo.findById(id)
          .orElseThrow(() -> new BusinessException(BillingException.PAYMENT_NOT_FOUND));

      case PACKAGE -> packageRepo.findById(id)
          .orElseThrow(() -> new BusinessException(BillingException.PAYMENT_NOT_FOUND));
    };
  }

  public void savePayment(PaymentType type, BasePayment payment) {
    switch (type) {
      case SINGLE -> singleRepo.save((SingleSwapPayment) payment);
      case PACKAGE -> packageRepo.save((PackagePayment) payment);
    }
  }

  public boolean validateSignature(Map<String, String> params) throws Exception {
    String receivedHash = params.get("vnp_SecureHash");

    Map<String, String> filtered = params.entrySet().stream()
        .filter(e -> e.getKey().startsWith("vnp_"))
        .filter(e -> !e.getKey().equals("vnp_SecureHash"))
        .filter(e -> !e.getKey().equals("vnp_SecureHashType"))
        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

    String dataToHash = VnpaySignatureUtil.buildQuery(filtered);
    String computedHash = VnpaySignatureUtil.hmacSHA512(config.getHashSecret(), dataToHash);

    return computedHash.equalsIgnoreCase(receivedHash);
  }
}
