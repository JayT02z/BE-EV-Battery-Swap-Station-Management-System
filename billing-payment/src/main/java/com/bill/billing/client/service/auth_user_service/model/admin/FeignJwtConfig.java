package com.bill.billing.client.service.auth_user_service.model.admin;

import com.bill.billing.client.config.SimpleJwtTokenGenerator;
import feign.RequestInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class FeignJwtConfig {
  private final SimpleJwtTokenGenerator jwtTokenGenerator;

  @Value("${jwt.admin.email}")
  private String adminEmail;

  @Value("${jwt.admin.role}")
  private String adminRole;

  @Bean
  public RequestInterceptor jwtFeignInterceptor() {
    return requestTemplate -> {
      String token = jwtTokenGenerator.generateToken1Min(
          adminEmail,
          adminRole
      );

      requestTemplate.header(
          "Authorization",
          "Bearer " + token
      );
    };
  }
}
