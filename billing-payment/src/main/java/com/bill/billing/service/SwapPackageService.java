package com.bill.billing.service;

import com.bill.billing.model.entity.SwapPackage;
import com.bill.billing.model.response.ResponseData;
import java.util.List;
import org.springframework.http.ResponseEntity;

public interface SwapPackageService {
  void decreaseDurationDaily();

  ResponseEntity<ResponseData<SwapPackage>> createPackage(SwapPackage swapPackage);

  ResponseEntity<ResponseData<SwapPackage>> extendPackage(Long id, Integer extraDays);

  ResponseEntity<ResponseData<String>> deletePackage(Long id);

  ResponseEntity<ResponseData<List<SwapPackage>>> getAllPackages();

  ResponseEntity<ResponseData<SwapPackage>> getPackageById(Long id);
}
