package com.bill.billing.repository;

import com.bill.billing.model.entity.BasePayment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BasePaymentRepository extends JpaRepository<BasePayment, Long> {
}
