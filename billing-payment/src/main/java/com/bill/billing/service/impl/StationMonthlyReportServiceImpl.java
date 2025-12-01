package com.bill.billing.service.impl;

import com.bill.billing.model.entity.SingleSwapPayment;
import com.bill.billing.model.entity.StationMonthlyReport;
import com.bill.billing.repository.SingleSwapPaymentRepository;
import com.bill.billing.repository.StationMonthlyReportRepository;
import com.bill.billing.service.StationMonthlyReportService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class StationMonthlyReportServiceImpl implements StationMonthlyReportService {

  private final SingleSwapPaymentRepository singleSwapPaymentRepository;
  private final StationMonthlyReportRepository stationMonthlyReportRepository;

  /**
   * Cron job: chạy tự động 00:01 ngày đầu tháng
   */
  @Override
  @Scheduled(cron = "0 1 0 1 * ?")
  public void generateMonthlyReportAutomatically() {
    generateMonthlyReportForLastMonth();
    log.info(">>> Station monthly report generated automatically.");
  }

  /**
   * Tạo báo cáo cho tháng trước
   */
  @Override
  @Transactional
  public void generateMonthlyReportForLastMonth() {
    LocalDate now = LocalDate.now();
    LocalDate lastMonth = now.minusMonths(1);

    int year = lastMonth.getYear();
    int month = lastMonth.getMonthValue();

    // Lấy tất cả SingleSwapPayment trong tháng
    List<SingleSwapPayment> payments = singleSwapPaymentRepository.findAll().stream()
        .filter(p -> p.getPaymentTime() != null &&
            p.getPaymentTime().getYear() == year &&
            p.getPaymentTime().getMonthValue() == month)
        .collect(Collectors.toList());

    // Group theo stationId (String)
    Map<String, List<SingleSwapPayment>> grouped = payments.stream()
        .collect(Collectors.groupingBy(p -> String.valueOf(p.getStationId())));

    for (Map.Entry<String, List<SingleSwapPayment>> entry : grouped.entrySet()) {
      String stationId = entry.getKey();
      List<SingleSwapPayment> stationPayments = entry.getValue();

      double totalRevenue = stationPayments.stream()
          .mapToDouble(p -> Optional.ofNullable(p.getTotalAmount()).orElse(0.0))
          .sum();

      long transactionCount = stationPayments.size();
      double averageRevenue = transactionCount == 0 ? 0 : totalRevenue / transactionCount;

      // Nếu report đã tồn tại thì update, chưa thì tạo mới
      StationMonthlyReport report = stationMonthlyReportRepository
          .findByStationIdAndYearAndMonth(stationId, year, month)
          .orElseGet(() -> StationMonthlyReport.builder()
              .stationId(stationId)
              .year(year)
              .month(month)
              .build());

      report.setRevenue(totalRevenue);
      report.setTransactions(transactionCount);
      report.setAverageRevenue(averageRevenue);
      report.setUpdatedAt(LocalDateTime.now());

      stationMonthlyReportRepository.save(report);
    }
  }
}
