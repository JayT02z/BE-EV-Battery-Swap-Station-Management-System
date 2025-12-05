package com.bill.billing.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CacheInvalidationEvent implements Serializable {
  // Data type need to refresh: "PRICE_POLICY", "BAGGAGE", "AIRPORT"
  private String dataType;

  // Action: "UPDATE", "DELETE", "CREATE"
  private String action;

  // ID của resource (nếu muốn refresh cụ thể 1 cái, để null nếu refresh all)
  private String resourceId;

  private LocalDateTime timestamp;
}
