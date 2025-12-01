package com.bill.billing.enums.auth_user_service;

public enum UserStatus {
  /**
   * Chờ admin phê duyệt đơn đăng ký
   */
  PENDING_APPROVAL,

  /**
   * Đơn đăng ký bị từ chối
   */
  REJECTED,

  /**
   * Chờ xác thực OTP (sau khi admin approve)
   */
  PENDING_VERIFICATION,

  /**
   * Tài khoản đang hoạt động
   */
  ACTIVE,

  /**
   * Tài khoản bị tạm khóa
   */
  INACTIVE,

  /**
   * Tài khoản bị cấm vĩnh viễn
   */
  BANNED
}
