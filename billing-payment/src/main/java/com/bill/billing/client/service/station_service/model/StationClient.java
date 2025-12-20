package com.bill.billing.client.service.station_service.model;

import com.bill.billing.client.service.auth_user_service.model.admin.FeignJwtConfig;
import com.bill.billing.client.service.station_service.model.response.StationDto;
import com.bill.billing.client.service.station_service.model.response.StationSwapSummaryDto;
import com.bill.billing.model.response.ResponseData;
import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name ="station-service", configuration = FeignJwtConfig.class)
public interface StationClient {
  @PostMapping("/api/admin/api/stations/getbycode/{stationId}")
  ResponseEntity<ResponseData<StationDto>> getStationByCode(
      @PathVariable String stationId);

  @GetMapping("/api/admin/api/swaplog/getallsumary")
  ResponseData<List<StationSwapSummaryDto>> getAllSwapSummary();
}
