package com.boilerplate.auth.service;

import com.boilerplate.auth.model.event.EmailEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

/**
 * Kafka Listener cho Email Events
 * Chỉ được kích hoạt khi app.kafka.enabled=true
 */
@Component
@ConditionalOnProperty(name = "app.kafka.enabled", havingValue = "true")
@RequiredArgsConstructor
@Slf4j
public class EmailKafkaListener {

    private final EmailService emailService;

    /**
     * Lắng nghe message từ Kafka và gửi email
     * Kafka giúp:
     * - Xử lý bất đồng bộ, không block API response
     * - Retry tự động khi gửi email thất bại
     * - Scale tốt khi có nhiều email cần gửi
     */
    @KafkaListener(
        topics = "email-topic",
        groupId = "auth-user-service",
        containerFactory = "emailEventKafkaListenerContainerFactory"
    )
    public void consumeEmailEvent(EmailEvent emailEvent) {
        try {
            emailService.sendEmailDirect(emailEvent.getTo(), emailEvent.getSubject(), emailEvent.getBody());
            log.info("✅ Đã gửi email thành công đến: {} (via Kafka)", emailEvent.getTo());
        } catch (Exception e) {
            log.error("❌ Lỗi khi gửi email đến: {}", emailEvent.getTo(), e);
            // TODO: Có thể push vào Dead Letter Queue (DLQ) để xử lý sau
        }
    }
}

