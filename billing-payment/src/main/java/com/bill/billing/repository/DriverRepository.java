package com.bill.billing.repository;

import com.bill.billing.model.event.entity.Driver;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DriverRepository extends JpaRepository<Driver, Integer> {
    Optional<Driver> findByEmployeeId(String employeeId);
}
