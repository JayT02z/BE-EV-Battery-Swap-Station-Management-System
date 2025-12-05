package com.station.station.repository;

import com.station.station.model.entity.BatterySwapStationLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BatterySwapStationLogRepository extends JpaRepository<BatterySwapStationLog, Long> {
}
