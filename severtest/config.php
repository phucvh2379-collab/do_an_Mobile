<?php
// config.php

// Thông tin kết nối MySQL
$db_host = "localhost";      // hoặc IP server MySQL
$db_user = "root";           // tên user MySQL
$db_pass = "123456789";      // mật khẩu MySQL
$db_name = "carbon_tracker"; // tên database

// Tạo kết nối (mysqli)
$conn = new mysqli($db_host, $db_user, $db_pass, $db_name);

// Kiểm tra kết nối
if ($conn->connect_error) {
    // Nếu kết nối lỗi, trả JSON lỗi và dừng script
    http_response_code(500);
    echo json_encode([
        "success" => false,
        "message" => "Database connection failed: " . $conn->connect_error
    ]);
    exit();
}

// Đảm bảo charset utf8 (nếu cần để hỗ trợ tiếng Việt)
$conn->set_charset("utf8mb4");
?>
