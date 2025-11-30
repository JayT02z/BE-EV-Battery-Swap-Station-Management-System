package com.boilerplate.billing.client.service.auth_user_service.model.admin;

import com.boilerplate.billing.client.service.auth_user_service.model.admin.response.UserResponse;
import com.boilerplate.billing.model.response.ResponseData;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name ="auth-user")
public interface AuthUserServiceClient {
  @GetMapping("/drivers/{employeeId}")
  ResponseData<UserResponse> getDriverByEmployeeId(@PathVariable("employeeId") String employeeId);
}
