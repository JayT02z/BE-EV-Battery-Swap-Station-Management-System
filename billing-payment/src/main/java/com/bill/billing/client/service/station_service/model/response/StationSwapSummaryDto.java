package com.bill.billing.client.service.station_service.model.response;


import com.bill.billing.enums.TimeSlot;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class StationSwapSummaryDto {
    private Long stationId;
    private String stationName;
    private String date; // yyyy-MM-dd
    private Map<TimeSlot, Integer> swapCountBySlot;
}
