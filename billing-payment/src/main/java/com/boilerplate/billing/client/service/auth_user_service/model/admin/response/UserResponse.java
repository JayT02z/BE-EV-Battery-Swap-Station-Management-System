package com.boilerplate.billing.client.service.auth_user_service.model.admin.response;

import com.boilerplate.billing.enums.auth_user_service.Role;
import com.boilerplate.billing.enums.auth_user_service.UserStatus;
import com.boilerplate.billing.enums.auth_user_service.VehicleStatus;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponse {
  private Long id;
  private String email;
  private String phone;
  private String fullName;
  private LocalDate birthday;
  private String avatar;
  private Role role;
  private String address;
  private String identityCard;
  private Boolean isVerified;
  private Boolean isActive;
  private UserStatus status;
  private String employeeId;
  private List<VehicleResponse> vehicles;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;


  @Data
  public static class VehicleResponse {
    private String vehicleId; // Mã xe công khai (EV45141124VFe34)
    private String vin;
    private String model;
    private String licensePlate;
    private String batteryType;
    private Double batteryCapacity;
    private VehicleStatus status;
    private String notes;
    private String imageUrl; // URL ảnh xe từ AWS S3

    // Thông tin chủ sở hữu (nếu có)
    private String employeeId; // Mã nhân viên của tài xế được cấp phát
    private String driverName; // Tên tài xế được cấp phát

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

  }
}
