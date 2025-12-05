package com.station.station.model.DTO;

import com.station.station.enums.BatteryStatus;
import com.station.station.enums.OwnerType;
import com.station.station.model.entity.Battery;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BatteryDTO {
    private Long id;
    private String batteryCode;
    private String model;
    private Double capacity; // Wh
    private Double soh;      // State of Health (%)
    private Double soc;      // State of Charge (%)
    private BatteryStatus status;
    private boolean isHold;


    private OwnerType ownerType;
    private String referenceId;

    public static BatteryDTO fromEntity(Battery battery) {
        if (battery == null) return null;

        return BatteryDTO.builder()
                .id(battery.getId())
                .batteryCode(battery.getBatteryCode())
                .model(battery.getModel())
                .capacity(battery.getCapacity())
                .soh(battery.getSoh())
                .soc(battery.getSoc())
                .status(battery.getStatus())
                .ownerType(battery.getOwnerType())
                .referenceId(battery.getReferenceId())
                .isHold(battery.isHold())
                .build();
    }
}
