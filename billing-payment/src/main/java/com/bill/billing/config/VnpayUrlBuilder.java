package com.bill.billing.config;

import com.bill.billing.utils.VnpaySignatureUtil;
import jakarta.servlet.http.HttpServletRequest;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import org.springframework.context.annotation.Configuration;

@Configuration
public class VnpayUrlBuilder {
  private final VnpayConfigProperties config;

  public VnpayUrlBuilder(VnpayConfigProperties config) {
    this.config = config;
  }

  public String buildPaymentUrl(Long paymentId, Long amount, String type, HttpServletRequest request) throws Exception {

    Map<String, String> params = new HashMap<>();
    params.put("vnp_Version", "2.1.0");
    params.put("vnp_Command", "pay");
    params.put("vnp_TmnCode", config.getTmnCode());
    params.put("vnp_Amount", String.valueOf(amount * 100));
    params.put("vnp_CurrCode", "VND");
    params.put("vnp_TxnRef", String.valueOf(paymentId));
    params.put("vnp_OrderInfo", "Thanh toan don hang #" + paymentId);
    params.put("vnp_OrderType", "billpayment");
    params.put("vnp_Locale", "vn");
    params.put("vnp_ReturnUrl", config.getReturnUrl() + "?type=" + type);
    params.put("vnp_IpAddr", request.getRemoteAddr());
    params.put("vnp_CreateDate",
        LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")));

    // Generate hash
    String hashData = VnpaySignatureUtil.buildQuery(params);
    String secureHash = VnpaySignatureUtil.hmacSHA512(config.getHashSecret(), hashData);

    params.put("vnp_SecureHash", secureHash);

    return config.getPayUrl() + "?" + VnpaySignatureUtil.buildQuery(params);
  }
}
