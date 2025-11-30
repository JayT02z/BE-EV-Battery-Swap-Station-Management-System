-- Migration: Tạo bảng stations trong billing-service
-- Mục đích: Đồng bộ dữ liệu từ station-inventory-service qua NiFi
-- Cấu trúc: Giống hệt bảng stations từ station-inventory-service
-- Date: 2025-11-23

CREATE TABLE IF NOT EXISTS public.stations (
    id BIGINT PRIMARY KEY, -- Không dùng SERIAL vì id được sync từ station-inventory-service

    -- Station Basic Info
    station_code VARCHAR(100),
    station_name VARCHAR(255),

    -- Location
    latitude DOUBLE PRECISION,
    longitude DOUBLE PRECISION,
    address TEXT,
    phone_number VARCHAR(20),

    -- Capacity
    total_slots INTEGER,
    available_slots INTEGER,

    -- Status
    status VARCHAR(50), -- ACTIVE, MAINTENANCE, OFFLINE

    -- Timestamps (thêm để tracking, có thể không có trong bảng gốc)
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Indexes để tối ưu performance
CREATE INDEX IF NOT EXISTS idx_stations_station_code ON stations(station_code);
CREATE INDEX IF NOT EXISTS idx_stations_status ON stations(status);
CREATE INDEX IF NOT EXISTS idx_stations_location ON stations(latitude, longitude);
CREATE INDEX IF NOT EXISTS idx_stations_available_slots ON stations(available_slots);

-- Trigger auto update updated_at
CREATE OR REPLACE FUNCTION fn_update_stations_updated_at()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

DROP TRIGGER IF EXISTS trg_stations_updated_at ON stations;
CREATE TRIGGER trg_stations_updated_at
BEFORE UPDATE ON stations
FOR EACH ROW
EXECUTE FUNCTION fn_update_stations_updated_at();

-- Comments
COMMENT ON TABLE stations IS 'Bảng stations đồng bộ từ station-inventory-service - dùng cho billing và analytics';
COMMENT ON COLUMN stations.id IS 'ID gốc từ station-inventory-service (không tự tăng)';
COMMENT ON COLUMN stations.station_code IS 'Mã trạm (unique identifier)';
COMMENT ON COLUMN stations.station_name IS 'Tên trạm';
COMMENT ON COLUMN stations.latitude IS 'Vĩ độ';
COMMENT ON COLUMN stations.longitude IS 'Kinh độ';
COMMENT ON COLUMN stations.address IS 'Địa chỉ trạm';
COMMENT ON COLUMN stations.phone_number IS 'Số điện thoại trạm';
COMMENT ON COLUMN stations.total_slots IS 'Tổng số slot pin';
COMMENT ON COLUMN stations.available_slots IS 'Số slot còn trống';
COMMENT ON COLUMN stations.status IS 'Trạng thái: ACTIVE, MAINTENANCE, OFFLINE';

