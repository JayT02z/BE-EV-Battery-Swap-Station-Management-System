package com.bill.billing.client.service.booking_service.model;

import com.bill.billing.client.service.auth_user_service.model.admin.FeignJwtConfig;
import com.bill.billing.model.response.ResponseData;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

@FeignClient(name ="booking-service", configuration = FeignJwtConfig.class)
public interface BookingClient {
  @PutMapping("/api/admin/api/bookings/confirmedIsPaid/{Id}")
  ResponseEntity<ResponseData<Void>> updateBookingStatus(@PathVariable("id") Long Id);
}
