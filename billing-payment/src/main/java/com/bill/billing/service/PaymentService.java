package com.bill.billing.service;

import com.bill.billing.model.DTO.CustomerPaymentsDTO;
import com.bill.billing.model.DTO.PackagePaymentDTO;
import com.bill.billing.model.DTO.SingleSwapPaymentDTO;
import com.bill.billing.model.request.PackagePaymentRequest;
import com.bill.billing.model.request.SingleSwapPaymentRequest;
import com.bill.billing.model.response.ResponseData;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface PaymentService {

    // PACKAGE PAYMENT
    ResponseEntity<ResponseData<List<PackagePaymentDTO>>> getAllPackagePayments();
    ResponseEntity<ResponseData<PackagePaymentDTO>> getPackagePaymentById(Long id);
    ResponseEntity<ResponseData<PackagePaymentDTO>> createPackagePayment(PackagePaymentRequest request);
    ResponseEntity<ResponseData<Void>> deletePackagePayment(Long id);
    ResponseEntity<ResponseData<Void>> confirmCashPackeagePayment(Long id);

    // SINGLE SWAP PAYMENT
    ResponseEntity<ResponseData<List<SingleSwapPaymentDTO>>> getAllSingleSwapPayments();
    ResponseEntity<ResponseData<SingleSwapPaymentDTO>> getSingleSwapPaymentById(Long id);
    ResponseEntity<ResponseData<SingleSwapPaymentDTO>> createSingleSwapPayment(SingleSwapPaymentRequest request);
    ResponseEntity<ResponseData<Void>> deleteSingleSwapPayment(Long id);
    ResponseEntity<ResponseData<Void>> confirmCashSingglePayment(Long id);

    // SUMMARY
    ResponseEntity<ResponseData<CustomerPaymentsDTO>> getAllPaymentsByCustomerId(String employeeId);

}
