package com.bill.billing.config;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "vnpay")
public class VnpayConfigProperties {
  private String tmnCode;
  private String hashSecret;
  private String payUrl;
  private String returnUrl;
}
