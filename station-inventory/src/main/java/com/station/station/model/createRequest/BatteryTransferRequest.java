package com.station.station.model.createRequest;

import lombok.Data;

import java.util.List;

@Data
public class BatteryTransferRequest {
    private List<String> batteryCodes;
    private String destinationStationCode;
}
