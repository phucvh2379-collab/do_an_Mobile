create database carbon_tracker;
CREATE TABLE user_data (
    id INT AUTO_INCREMENT PRIMARY KEY,           -- Khóa chính, tự tăng
    name VARCHAR(255),                           -- Tên người dùng
    age VARCHAR(10),                             -- Tuổi (lưu dưới dạng chuỗi để phù hợp với UserData)
    occupation VARCHAR(255),                     -- Nghề nghiệp
    month VARCHAR(10),                           -- Tháng (chuỗi)
    year VARCHAR(10),                            -- Năm (chuỗi)
    days_in_month INT,                           -- Số ngày trong tháng
    daily_co2 DOUBLE,                            -- Lượng CO₂ hàng ngày
    monthly_co2 DOUBLE,                          -- Lượng CO₂ hàng tháng
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP -- Thời gian tạo bản ghi
);
