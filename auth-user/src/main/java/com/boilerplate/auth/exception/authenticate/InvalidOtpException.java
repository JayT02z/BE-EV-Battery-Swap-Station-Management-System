package com.boilerplate.auth.exception.authenticate;

/**
 * Exception khi OTP không hợp lệ
 */
public class InvalidOtpException extends RuntimeException {
    public InvalidOtpException(String message) {
        super(message);
    }
}

