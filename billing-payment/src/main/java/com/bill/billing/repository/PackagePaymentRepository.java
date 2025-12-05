package com.bill.billing.repository;

import com.bill.billing.model.entity.PackagePayment;
import com.bill.billing.model.event.entity.Driver;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PackagePaymentRepository extends JpaRepository<PackagePayment, Long> {

    List<PackagePayment> findByCustomerId(Driver customerId);
}
