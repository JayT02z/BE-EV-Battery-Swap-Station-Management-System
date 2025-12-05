package com.bill.billing.model.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DriverDTO {
    private Long id;
    private String email;
    private String phone;
    private String fullName;
    private String birthday;      // từ service Auth trả "2025-11-16", kiểu String là OK
    private String address;
    private String identityCard;
    private String employeeId;


}
