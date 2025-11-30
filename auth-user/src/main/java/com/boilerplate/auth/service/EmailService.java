package com.boilerplate.auth.service;

import com.boilerplate.auth.enums.authen.OtpType;
import com.boilerplate.auth.model.event.EmailEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

/**
 * Service xử lý gửi email
 * - Sử dụng Thymeleaf templates cho email đẹp và dễ bảo trì
 * - Hỗ trợ MailHog cho development và SMTP thật cho production
 * - Tích hợp Kafka (qua EmailKafkaListener) khi enabled
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {

    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;
    private final KafkaProducerService kafkaProducerService;

    /**
     * Gửi email trực tiếp với HTML content
     * Hỗ trợ cả MailHog (dev) và SMTP thật (prod)
     * Public để EmailKafkaListener có thể gọi
     */
    public void sendEmailDirect(String to, String subject, String body) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(body, true);
        helper.setFrom("noreply@evbss.com");

        mailSender.send(message);
    }

    /**
     * Tạo nội dung email OTP bằng Thymeleaf template
     * Template: src/main/resources/templates/email/otp-email.html
     */
    public String buildOtpEmailBody(String fullName, String otp, OtpType otpType) {
        Context context = new Context();
        context.setVariable("fullName", fullName);
        context.setVariable("otpCode", otp);
        context.setVariable("purpose", otpType == OtpType.REGISTRATION ? "xác thực tài khoản" : "đặt lại mật khẩu");
        context.setVariable("year", java.time.Year.now().getValue());

        return templateEngine.process("email/otp-email", context);
    }

    /**
     * Tạo nội dung email chào mừng bằng Thymeleaf template
     * Template: src/main/resources/templates/email/welcome-email.html
     */
    public String buildWelcomeEmailBody(String fullName, String role) {
        Context context = new Context();
        context.setVariable("fullName", fullName);
        context.setVariable("role", role);
        context.setVariable("roleText", getRoleText(role));
        context.setVariable("year", java.time.Year.now().getValue());

        // Set các features theo role
        if ("DRIVER".equals(role)) {
            context.setVariable("features", java.util.List.of(
                "🔍 Tìm kiếm trạm đổi pin gần nhất",
                "📅 Đặt lịch đổi pin trước",
                "🚗 Quản lý phương tiện của bạn",
                "📊 Xem lịch sử giao dịch",
                "💳 Quản lý gói thuê pin"
            ));
        } else if ("STAFF".equals(role)) {
            context.setVariable("features", java.util.List.of(
                "📦 Quản lý tồn kho pin tại trạm",
                "🔄 Xử lý giao dịch đổi pin",
                "🔋 Ghi nhận trạng thái pin",
                "👥 Hỗ trợ khách hàng"
            ));
        } else if ("ADMIN".equals(role)) {
            context.setVariable("features", java.util.List.of(
                "🏢 Quản lý trạm và nhân viên",
                "📈 Xem báo cáo và thống kê",
                "⚙️ Cấu hình hệ thống",
                "👤 Quản lý người dùng"
            ));
        }

        return templateEngine.process("email/welcome-email", context);
    }

    /**
     * Tạo nội dung email thông báo đơn đăng ký đang chờ duyệt
     * Template: src/main/resources/templates/email/registration-pending-email.html
     */
    public String buildRegistrationPendingEmailBody(String fullName, String role) {
        Context context = new Context();
        context.setVariable("fullName", fullName);
        context.setVariable("roleText", getRoleText(role));
        context.setVariable("year", java.time.Year.now().getValue());

        return templateEngine.process("email/registration-pending-email", context);
    }

    /**
     * Gửi email OTP (xác thực hoặc reset password)
     */
    public void sendOtpEmail(String toEmail, String fullName, String otpCode, OtpType otpType) {
        String subject = otpType == OtpType.REGISTRATION
            ? "🔐 Mã OTP Xác Thực Tài Khoản - EV Battery Swap Station"
            : "🔐 Mã OTP Đặt Lại Mật Khẩu - EV Battery Swap Station";

        String body = buildOtpEmailBody(fullName, otpCode, otpType);

        // Gửi qua Kafka để xử lý bất đồng bộ
        EmailEvent emailEvent = EmailEvent.builder()
                .to(toEmail)
                .subject(subject)
                .body(body)
                .build();
        kafkaProducerService.sendEmailEvent(emailEvent);

        log.info("📧 Đã đưa OTP email vào Kafka queue: {} ({})", toEmail, otpType);
    }

    /**
     * Gửi email chào mừng sau khi tài khoản được kích hoạt
     */
    public void sendWelcomeEmail(String toEmail, String fullName, String role) {
        String subject = "🎉 Chào Mừng Đến Với EV Battery Swap Station!";
        String body = buildWelcomeEmailBody(fullName, role);

        // Gửi qua Kafka để xử lý bất đồng bộ
        EmailEvent emailEvent = EmailEvent.builder()
                .to(toEmail)
                .subject(subject)
                .body(body)
                .build();
        kafkaProducerService.sendEmailEvent(emailEvent);

        log.info("📧 Đã đưa welcome email vào Kafka queue: {}", toEmail);
    }

    /**
     * Gửi email thông báo đơn đăng ký đang chờ duyệt
     */
    public void sendRegistrationPendingEmail(String toEmail, String fullName, String role) {
        String subject = "⏳ Đơn Đăng Ký Đang Được Xử Lý - EV Battery Swap Station";
        String body = buildRegistrationPendingEmailBody(fullName, role);

        // Gửi qua Kafka để xử lý bất đồng bộ
        EmailEvent emailEvent = EmailEvent.builder()
                .to(toEmail)
                .subject(subject)
                .body(body)
                .build();
        kafkaProducerService.sendEmailEvent(emailEvent);

        log.info("📧 Đã đưa registration pending email vào Kafka queue: {}", toEmail);
    }

    /**
     * Chuyển đổi role code sang text tiếng Việt
     */
    private String getRoleText(String role) {
        return switch (role.toUpperCase()) {
            case "DRIVER" -> "Tài xế";
            case "STAFF" -> "Nhân viên trạm";
            case "ADMIN" -> "Quản trị viên";
            default -> role;
        };
    }
}
