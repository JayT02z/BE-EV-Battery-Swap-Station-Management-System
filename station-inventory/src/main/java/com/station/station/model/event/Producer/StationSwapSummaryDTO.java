package com.station.station.model.event.Producer;

import com.station.station.enums.TimeSlot;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class StationSwapSummaryDTO {
    private Long stationId;
    private String stationName;
    private String date; // yyyy-MM-dd
    private Map<TimeSlot, Integer> swapCountBySlot;
}
