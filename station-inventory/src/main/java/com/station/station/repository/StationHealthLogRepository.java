package com.station.station.repository;

import com.station.station.model.entity.StationHealthLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StationHealthLogRepository extends JpaRepository<StationHealthLog, Long> {
}
