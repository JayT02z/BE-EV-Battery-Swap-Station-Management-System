package com.bill.billing.client.service;

import com.bill.billing.client.config.SimpleJwtTokenGenerator;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class FeignJwtInterceptor implements RequestInterceptor {

  private final SimpleJwtTokenGenerator jwtTokenGenerator;

  @Value("${jwt.internal.service-name:billing-service}")
  private String serviceName;

  public FeignJwtInterceptor(SimpleJwtTokenGenerator jwtTokenGenerator) {
    this.jwtTokenGenerator = jwtTokenGenerator;
  }

  @Override
  public void apply(RequestTemplate template) {
    String token = jwtTokenGenerator.generateInternalToken(serviceName);
    template.header("Authorization", "Bearer " + token);
  }
}
