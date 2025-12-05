package com.bill.billing.repository;

import com.bill.billing.model.entity.SwapPackage;
import com.bill.billing.enums.PackageStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SwapPackageRepository extends JpaRepository<SwapPackage, Long> {
    List<SwapPackage> findByStatus(PackageStatus status);
}
