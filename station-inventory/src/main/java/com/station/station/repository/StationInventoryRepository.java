package com.station.station.repository;

import com.station.station.model.entity.StationInventory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StationInventoryRepository extends JpaRepository<StationInventory, Long> {
}
