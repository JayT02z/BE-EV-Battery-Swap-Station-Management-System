package com.station.station.repository;

import com.station.station.model.entity.BatteryReturnLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BatteryReturnLogRepository extends JpaRepository<BatteryReturnLog, Long> {
}
