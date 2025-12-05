package com.station.station.repository;

import com.station.station.model.event.Producer.BatterySwapSummary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BatterySwapSummaryRepository extends JpaRepository<BatterySwapSummary, Long> {
}
