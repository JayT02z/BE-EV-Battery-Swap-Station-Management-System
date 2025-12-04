package com.bill.billing.client.service.station_service.model.response;


import lombok.Data;

@Data
public class StationDto {
    private Long id;
    private String stationCode;
    private String stationName;
    private String address;
}
