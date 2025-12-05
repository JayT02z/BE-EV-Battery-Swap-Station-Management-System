package com.station.station.model.createRequest;

import com.station.station.enums.SupplyRequestStatus;
import lombok.Data;

@Data
public class UpdateSupplyStatusRequest {
    private SupplyRequestStatus status;
    private String adminNote;
}
