package com.bill.billing.consumers;

import com.bill.billing.client.service.cache_refresh_strategy.CacheRefreshStrategy;
import com.bill.billing.model.CacheInvalidationEvent;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class CentralCacheEvictionListener {
  private final Map<String, CacheRefreshStrategy> strategyMap;
  private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(10);
  private final Random random = new Random();

  public CentralCacheEvictionListener(List<CacheRefreshStrategy> strategies) {
    this.strategyMap = strategies.stream()
        .collect(Collectors.toMap(CacheRefreshStrategy::getSupportedDataType, Function.identity()));
  }

  @KafkaListener(topics = "cache-invalidation", groupId = "cache-refresh-group")
  public void handleEvent(CacheInvalidationEvent event) {
    // 1. Find the appropriate strategy
    CacheRefreshStrategy strategy = strategyMap.get(event.getDataType());
    if (strategy == null) {
      return;
    }

    // 2. Calculate random (to avoid thundering herd) delay between 10s to 30s)
    int delayMs = 10_000 + random.nextInt(20_000);

    // 3. Schedule the cache refresh task
    scheduler.schedule(() -> {
      try {
        strategy.refreshCache(event.getResourceId());
      } catch (Exception e) {
      }
    }, delayMs, TimeUnit.MILLISECONDS);
  }
}
