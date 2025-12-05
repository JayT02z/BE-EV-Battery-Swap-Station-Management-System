package com.station.station.repository;

import com.station.station.model.entity.ChargeLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChargeLogRepository extends JpaRepository<ChargeLog, Long> {
}
