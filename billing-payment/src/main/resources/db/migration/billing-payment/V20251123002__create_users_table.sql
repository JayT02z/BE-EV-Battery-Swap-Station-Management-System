-- Migration: Tạo bảng users trong billing-service
-- Mục đích: Đồng bộ dữ liệu từ auth-user-service qua NiFi
-- Cấu trúc: Giống hệt bảng users từ auth-user-service
-- Date: 2025-11-23

CREATE TABLE IF NOT EXISTS public.users (
    id BIGINT PRIMARY KEY, -- Không dùng SERIAL vì id được sync từ auth-user-service

    -- OAuth2 Fields
    google_id VARCHAR(100) UNIQUE,
    oauth_id VARCHAR(100),
    oauth_provider VARCHAR(20),

    -- Basic Info
    email VARCHAR(100) NOT NULL UNIQUE,
    phone VARCHAR(20),
    full_name VARCHAR(100) NOT NULL,
    birthday DATE,
    avatar VARCHAR(500),

    -- Role & Permissions
    role VARCHAR(20) NOT NULL,
    address VARCHAR(255),
    identity_card VARCHAR(20) UNIQUE,

    -- Account Status
    is_verified BOOLEAN NOT NULL DEFAULT FALSE,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    status VARCHAR(30) NOT NULL DEFAULT 'PENDING_APPROVAL',
    rejection_reason TEXT,

    -- Staff Assignment
    assigned_station_id BIGINT,

    -- Employee ID (từ V3)
    employee_id VARCHAR(20) UNIQUE,

    -- Timestamps
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    -- Constraints giống bảng gốc
    CONSTRAINT chk_role CHECK (role IN ('DRIVER', 'STAFF', 'ADMIN')),
    CONSTRAINT chk_status CHECK (status IN ('PENDING_APPROVAL', 'ACTIVE', 'INACTIVE', 'REJECTED'))
);

-- Indexes giống bảng gốc để tối ưu performance
CREATE INDEX IF NOT EXISTS idx_users_email ON users(email);
CREATE INDEX IF NOT EXISTS idx_users_google_id ON users(google_id);
CREATE INDEX IF NOT EXISTS idx_users_phone ON users(phone);
CREATE INDEX IF NOT EXISTS idx_users_status ON users(status);
CREATE INDEX IF NOT EXISTS idx_users_oauth ON users(oauth_id, oauth_provider);
CREATE INDEX IF NOT EXISTS idx_users_role ON users(role);
CREATE INDEX IF NOT EXISTS idx_users_employee_id ON users(employee_id);
CREATE INDEX IF NOT EXISTS idx_users_identity_card ON users(identity_card);

-- Trigger auto update updated_at
CREATE OR REPLACE FUNCTION fn_update_users_updated_at()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

DROP TRIGGER IF EXISTS trg_users_updated_at ON users;
CREATE TRIGGER trg_users_updated_at
BEFORE UPDATE ON users
FOR EACH ROW
EXECUTE FUNCTION fn_update_users_updated_at();

-- Comments
COMMENT ON TABLE users IS 'Bảng users đồng bộ từ auth-user-service - dùng cho billing và analytics';
COMMENT ON COLUMN users.id IS 'ID gốc từ auth-user-service (không tự tăng)';
COMMENT ON COLUMN users.google_id IS 'Google ID từ OAuth2';
COMMENT ON COLUMN users.oauth_id IS 'OAuth ID từ provider (Google, Facebook, etc.)';
COMMENT ON COLUMN users.oauth_provider IS 'OAuth Provider (GOOGLE, FACEBOOK, etc.)';
COMMENT ON COLUMN users.email IS 'Email - identifier chính cho OAuth2';
COMMENT ON COLUMN users.is_verified IS 'Email đã được xác thực chưa';
COMMENT ON COLUMN users.is_active IS 'Tài khoản có đang hoạt động không';
COMMENT ON COLUMN users.status IS 'Trạng thái phê duyệt: PENDING_APPROVAL, ACTIVE, INACTIVE, REJECTED';
COMMENT ON COLUMN users.assigned_station_id IS 'ID trạm được phân công (chỉ cho STAFF)';
COMMENT ON COLUMN users.employee_id IS 'Mã nhân viên (EVD/EVS + 6 chữ số)';

