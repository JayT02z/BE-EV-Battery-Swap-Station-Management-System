package com.bill.billing.client.service.cache_refresh_strategy;

public interface CacheRefreshStrategy {
  String getSupportedDataType();
  void refreshCache(String resourceId);
}
