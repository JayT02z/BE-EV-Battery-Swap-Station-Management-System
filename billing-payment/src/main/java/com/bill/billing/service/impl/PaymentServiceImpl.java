package com.bill.billing.service.impl;

import com.bill.billing.client.service.auth_user_service.model.admin.AuthUserServiceClient;
import com.bill.billing.clients.BookingClient;
import com.bill.billing.enums.PaymentStatus;
import com.bill.billing.model.DTO.CustomerPaymentsDTO;
import com.bill.billing.model.DTO.PackagePaymentDTO;
import com.bill.billing.model.DTO.SingleSwapPaymentDTO;
import com.bill.billing.model.entity.PackagePayment;
import com.bill.billing.model.entity.SingleSwapPayment;
import com.bill.billing.model.event.entity.Driver;
import com.bill.billing.model.request.PackagePaymentRequest;
import com.bill.billing.model.request.SingleSwapPaymentRequest;
import com.bill.billing.model.response.ResponseData;
import com.bill.billing.repository.DriverRepository;
import com.bill.billing.repository.PackagePaymentRepository;
import com.bill.billing.repository.SingleSwapPaymentRepository;
import com.bill.billing.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

  private final PackagePaymentRepository packagePaymentRepository;
  private final SingleSwapPaymentRepository singleSwapPaymentRepository;
  private final AuthUserServiceClient authUserClient;
  private final DriverRepository driverRepository;
  private final BookingClient bookingClient;

  //================ PACKAGE PAYMENT ======================
  @Override
  public ResponseEntity<ResponseData<List<PackagePaymentDTO>>> getAllPackagePayments() {
    List<PackagePaymentDTO> payments = packagePaymentRepository.findAll()
        .stream().map(PackagePaymentDTO::fromEntity)
        .collect(Collectors.toList());
    return ResponseEntity.ok(new ResponseData<>(HttpStatus.OK.value(),
        "Fetched all package payments successfully", payments));
  }

  @Override
  public ResponseEntity<ResponseData<PackagePaymentDTO>> getPackagePaymentById(Long id) {
    Optional<PackagePayment> found = packagePaymentRepository.findById(id);
    if (found.isEmpty()) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND)
          .body(new ResponseData<>(HttpStatus.NOT_FOUND.value(), "Package payment not found", null));
    }
    return ResponseEntity.ok(new ResponseData<>(HttpStatus.OK.value(), "Package payment found",
        PackagePaymentDTO.fromEntity(found.get())));
  }

  @Override
  public ResponseEntity<ResponseData<PackagePaymentDTO>> createPackagePayment(PackagePaymentRequest request) {

    PackagePayment payment = new PackagePayment();

    // Lấy thông tin từ AuthService
    var response = authUserClient.getDriverByEmployeeId(request.getCustomerId());

    if (response == null || response.getData() == null) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND)
          .body(new ResponseData<>(HttpStatus.NOT_FOUND.value(), "Driver không tồn tại", null));
    }

    var userResponse = response.getData();

    // Upsert driver
    Driver driver = driverRepository.findByEmployeeId(userResponse.getEmployeeId()).orElse(new Driver());
    driver.setEmail(userResponse.getEmail());
    driver.setPhone(userResponse.getPhone());
    driver.setFullName(userResponse.getFullName());
    driver.setAddress(userResponse.getAddress());
    driver.setIdentityCard(userResponse.getIdentityCard());
    driver.setEmployeeId(userResponse.getEmployeeId());
    driver.setBirthday(userResponse.getBirthday());
    driver = driverRepository.save(driver);

    // Map payment
    payment.setCustomerId(driver);
    payment.setTotalAmount(request.getTotalAmount());
    payment.setBaseAmount(request.getBaseAmount());
    payment.setDiscountAmount(request.getDiscountAmount());
    payment.setTaxAmount(request.getTaxAmount());
    payment.setMethod(request.getMethod());
    payment.setStatus(request.getStatus());
    payment.setBookingId(request.getBookingId());
    payment.setDescription(request.getDescription());
    payment.setPaymentTime(request.getPaymentTime());
    payment.setPackageId(request.getPackageId());
    payment.setStartDate(request.getStartDate());
    payment.setEndDate(request.getEndDate());

    // Save
    PackagePayment saved = packagePaymentRepository.save(payment);

    return ResponseEntity.status(HttpStatus.CREATED)
        .body(new ResponseData<>(HttpStatus.CREATED.value(), "Package payment created successfully",
            PackagePaymentDTO.fromEntity(saved)));
  }

  @Override
  public ResponseEntity<ResponseData<Void>> deletePackagePayment(Long id) {
    if (!packagePaymentRepository.existsById(id)) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND)
          .body(new ResponseData<>(HttpStatus.NOT_FOUND.value(), "Package payment not found", null));
    }
    packagePaymentRepository.deleteById(id);
    return ResponseEntity.ok(new ResponseData<>(HttpStatus.OK.value(), "Package payment deleted successfully", null));
  }


  @Override
  public ResponseEntity<ResponseData<Void>> confirmCashPackeagePayment(Long id){
    PackagePayment payment = packagePaymentRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Booking không tồn tại"));
    payment.setStatus(PaymentStatus.SUCCESS);
    bookingClient.updateBookingStatus(payment.getBookingId());
    packagePaymentRepository.save(payment);
    return ResponseEntity.ok(new ResponseData<>(HttpStatus.OK.value(), "Payment confirmed successfully", null));
  }

  //================ SINGLE SWAP PAYMENT ======================
  @Override
  public ResponseEntity<ResponseData<List<SingleSwapPaymentDTO>>> getAllSingleSwapPayments() {
    List<SingleSwapPaymentDTO> payments = singleSwapPaymentRepository.findAll()
        .stream().map(SingleSwapPaymentDTO::fromEntity)
        .collect(Collectors.toList());
    return ResponseEntity.ok(new ResponseData<>(HttpStatus.OK.value(),
        "Fetched all single swap payments successfully", payments));
  }

  @Override
  public ResponseEntity<ResponseData<SingleSwapPaymentDTO>> getSingleSwapPaymentById(Long id) {
    Optional<SingleSwapPayment> found = singleSwapPaymentRepository.findById(id);
    if (found.isEmpty()) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND)
          .body(new ResponseData<>(HttpStatus.NOT_FOUND.value(), "Single swap payment not found", null));
    }
    return ResponseEntity.ok(new ResponseData<>(HttpStatus.OK.value(), "Single swap payment found",
        SingleSwapPaymentDTO.fromEntity(found.get())));
  }

  @Override
  public ResponseEntity<ResponseData<SingleSwapPaymentDTO>> createSingleSwapPayment(SingleSwapPaymentRequest request) {

    SingleSwapPayment payment = new SingleSwapPayment();

    var response = authUserClient.getDriverByEmployeeId(request.getCustomerId());

    if (response == null || response.getData() == null) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND)
          .body(new ResponseData<>(HttpStatus.NOT_FOUND.value(), "Driver không tồn tại", null));
    }

    var userResponse = response.getData();

    Driver driver = driverRepository.findByEmployeeId(userResponse.getEmployeeId()).orElse(new Driver());
    driver.setEmail(userResponse.getEmail());
    driver.setPhone(userResponse.getPhone());
    driver.setFullName(userResponse.getFullName());
    driver.setAddress(userResponse.getAddress());
    driver.setIdentityCard(userResponse.getIdentityCard());
    driver.setEmployeeId(userResponse.getEmployeeId());
    driver.setBirthday(userResponse.getBirthday());
    driver = driverRepository.save(driver);

    payment.setCustomerId(driver);
    payment.setTotalAmount(request.getTotalAmount());
    payment.setBaseAmount(request.getBaseAmount());
    payment.setDiscountAmount(request.getDiscountAmount());
    payment.setTaxAmount(request.getTaxAmount());
    payment.setMethod(request.getMethod());
    payment.setStatus(request.getStatus());
    payment.setBookingId(request.getBookingId());
    payment.setDescription(request.getDescription());
    payment.setPaymentTime(request.getPaymentTime());
    payment.setStationId(request.getStationId());
    payment.setPackageId(request.getPackageId());

    SingleSwapPayment saved = singleSwapPaymentRepository.save(payment);

    return ResponseEntity.status(HttpStatus.CREATED)
        .body(new ResponseData<>(HttpStatus.CREATED.value(), "Single swap payment created successfully",
            SingleSwapPaymentDTO.fromEntity(saved)));
  }

  @Override
  public ResponseEntity<ResponseData<Void>> deleteSingleSwapPayment(Long id) {
    if (!singleSwapPaymentRepository.existsById(id)) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND)
          .body(new ResponseData<>(HttpStatus.NOT_FOUND.value(), "Single swap payment not found", null));
    }
    singleSwapPaymentRepository.deleteById(id);
    return ResponseEntity.ok(new ResponseData<>(HttpStatus.OK.value(), "Single swap payment deleted successfully", null));
  }

  @Override
  public ResponseEntity<ResponseData<Void>> confirmCashSingglePayment(Long id){
    SingleSwapPayment payment = singleSwapPaymentRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Booking không tồn tại"));
    payment.setStatus(PaymentStatus.SUCCESS);
    singleSwapPaymentRepository.save(payment);
    bookingClient.updateBookingStatus(payment.getBookingId());
    return ResponseEntity.ok(new ResponseData<>(HttpStatus.OK.value(), "Payment confirmed successfully", null));
  }

  //================ SUMMARY FOR CUSTOMER ======================
  @Override
  public ResponseEntity<ResponseData<CustomerPaymentsDTO>> getAllPaymentsByCustomerId(String employeeId) {

    Driver driver = driverRepository.findByEmployeeId(employeeId)
        .orElseThrow(() -> new RuntimeException("Customer không tồn tại"));

    List<SingleSwapPayment> swapPayments = singleSwapPaymentRepository.findByCustomerId(driver);
    List<PackagePayment> packagePayments = packagePaymentRepository.findByCustomerId(driver);

    double totalAmount = swapPayments.stream().mapToDouble(p -> p.getTotalAmount() != null ? p.getTotalAmount() : 0.0).sum()
        + packagePayments.stream().mapToDouble(p -> p.getTotalAmount() != null ? p.getTotalAmount() : 0.0).sum();

    CustomerPaymentsDTO response = CustomerPaymentsDTO.builder()
        .customerId(driver.getEmployeeId())
        .customerName(driver.getFullName())
        .swapPayments(swapPayments.stream().map(SingleSwapPaymentDTO::fromEntity).toList())
        .packagePayments(packagePayments.stream().map(PackagePaymentDTO::fromEntity).toList())
        .totalSwapPayments(swapPayments.size())
        .totalPackagePayments(packagePayments.size())
        .totalAmount(totalAmount)
        .build();

    return ResponseEntity.ok(new ResponseData<>(HttpStatus.OK.value(),
        "Lấy tất cả thanh toán của customer thành công", response));
  }

}
