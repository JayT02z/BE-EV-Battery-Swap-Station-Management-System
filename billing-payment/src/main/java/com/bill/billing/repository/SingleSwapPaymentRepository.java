package com.bill.billing.repository;

import com.bill.billing.model.entity.SingleSwapPayment;
import com.bill.billing.model.event.entity.Driver;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SingleSwapPaymentRepository extends JpaRepository<SingleSwapPayment, Long> {

    List<SingleSwapPayment> findByCustomerId(Driver customerId);
}
