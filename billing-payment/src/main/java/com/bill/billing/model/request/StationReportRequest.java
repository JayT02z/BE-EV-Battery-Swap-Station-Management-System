package com.bill.billing.model.request;

import lombok.Data;

import java.util.List;

@Data
public class StationReportRequest {
    private List<Object> report;
}
