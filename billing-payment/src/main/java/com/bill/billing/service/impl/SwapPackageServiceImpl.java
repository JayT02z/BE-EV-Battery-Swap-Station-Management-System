package com.bill.billing.service.impl;

import com.bill.billing.enums.PackageStatus;
import com.bill.billing.exception.BillingException;
import com.bill.billing.exception.BusinessException;
import com.bill.billing.model.entity.SwapPackage;
import com.bill.billing.model.response.ResponseData;
import com.bill.billing.repository.SwapPackageRepository;
import com.bill.billing.service.SwapPackageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class SwapPackageServiceImpl implements SwapPackageService {

  private final SwapPackageRepository swapPackageRepository;

  @Override
  @Scheduled(cron = "0 0 0 * * *")
  public void decreaseDurationDaily() {
    List<SwapPackage> activePackages = swapPackageRepository.findByStatus(PackageStatus.ACTIVE);
    for (SwapPackage pkg : activePackages) {
      if (pkg.getDurationDays() != null && pkg.getDurationDays() > 0) {
        pkg.setDurationDays(pkg.getDurationDays() - 1);
        pkg.setUpdatedAt(LocalDateTime.now());

        if (pkg.getDurationDays() <= 0) {
          pkg.setStatus(PackageStatus.EXPIRED);
        }
        swapPackageRepository.save(pkg);
      }
    }
  }

  @Override
  public ResponseEntity<ResponseData<SwapPackage>> createPackage(SwapPackage swapPackage) {
    if (swapPackage.getPackageName() == null || swapPackage.getTotalSwaps() == null) {
      throw new BusinessException(BillingException.VALIDATION_FAILED);
    }

    swapPackage.setCreatedAt(LocalDateTime.now());
    swapPackage.setUpdatedAt(LocalDateTime.now());
    swapPackageRepository.save(swapPackage);

    ResponseData<SwapPackage> response = ResponseData.<SwapPackage>builder()
        .statusCode(HttpStatus.CREATED.value())
        .message("Tạo gói thuê pin thành công")
        .data(swapPackage)
        .build();

    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }

  @Override
  public ResponseEntity<ResponseData<SwapPackage>> extendPackage(Long id, Integer extraDays) {
    SwapPackage existing = swapPackageRepository.findById(id)
        .orElseThrow(() -> new BusinessException(BillingException.PACKAGE_NOT_FOUND));

    if (existing.getStatus() == null) {
      throw new BusinessException(BillingException.PACKAGE_INACTIVE);
    }

    if (extraDays == null || extraDays <= 0) {
      throw new BusinessException(BillingException.BAD_REQUEST);
    }

    existing.setDurationDays(existing.getDurationDays() + extraDays);
    existing.setUpdatedAt(LocalDateTime.now());
    swapPackageRepository.save(existing);

    ResponseData<SwapPackage> response = ResponseData.<SwapPackage>builder()
        .statusCode(HttpStatus.OK.value())
        .message("Gia hạn gói thuê pin thành công")
        .data(existing)
        .build();

    return ResponseEntity.ok(response);
  }

  @Override
  public ResponseEntity<ResponseData<String>> deletePackage(Long id) {
    SwapPackage existing = swapPackageRepository.findById(id)
        .orElseThrow(() -> new BusinessException(BillingException.PACKAGE_NOT_FOUND));

    swapPackageRepository.delete(existing);

    ResponseData<String> response = ResponseData.<String>builder()
        .statusCode(HttpStatus.OK.value())
        .message("Xóa gói thuê pin thành công")
        .data("Đã xóa gói có ID: " + id)
        .build();

    return ResponseEntity.ok(response);
  }

  @Override
  public ResponseEntity<ResponseData<List<SwapPackage>>> getAllPackages() {
    List<SwapPackage> packages = swapPackageRepository.findAll();

    if (packages.isEmpty()) {
      throw new BusinessException(BillingException.PACKAGE_NOT_FOUND);
    }

    ResponseData<List<SwapPackage>> response = ResponseData.<List<SwapPackage>>builder()
        .statusCode(HttpStatus.OK.value())
        .message("Lấy danh sách gói thuê pin thành công")
        .data(packages)
        .build();

    return ResponseEntity.ok(response);
  }

  @Override
  public ResponseEntity<ResponseData<SwapPackage>> getPackageById(Long id) {
    SwapPackage existing = swapPackageRepository.findById(id)
        .orElseThrow(() -> new BusinessException(BillingException.PACKAGE_NOT_FOUND));

    ResponseData<SwapPackage> response = ResponseData.<SwapPackage>builder()
        .statusCode(HttpStatus.OK.value())
        .message("Lấy thông tin gói thuê pin thành công")
        .data(existing)
        .build();

    return ResponseEntity.ok(response);
  }
}
