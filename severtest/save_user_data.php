<?php
// save_user_data.php

// Cho phép cross origin (nếu Android và server khác domain)
// Bạn có thể tùy chỉnh origin cụ thể nếu cần
header("Access-Control-Allow-Origin: *");
header("Content-Type: application/json; charset=UTF-8");

// Chỉ chấp nhận phương thức POST
if ($_SERVER["REQUEST_METHOD"] !== "POST") {
    http_response_code(405);
    echo json_encode(["success" => false, "message" => "Method not allowed"]);
    exit();
}

// Đọc dữ liệu JSON từ body
$data = json_decode(file_get_contents("php://input"), true);
if ($data === null) {
    http_response_code(400);
    echo json_encode(["success" => false, "message" => "Invalid JSON"]);
    exit();
}

// Lấy các trường dữ liệu cần thiết
$name = isset($data["name"]) ? $data["name"] : null;
$age = isset($data["age"]) ? $data["age"] : null;
$occupation = isset($data["occupation"]) ? $data["occupation"] : null;
$month = isset($data["month"]) ? $data["month"] : null;
$year = isset($data["year"]) ? $data["year"] : null;
$daysInMonth = isset($data["daysInMonth"]) ? $data["daysInMonth"] : null;
$dailyCO2 = isset($data["dailyCO2"]) ? $data["dailyCO2"] : null;
$monthlyCO2 = isset($data["monthlyCO2"]) ? $data["monthlyCO2"] : null;

// Kiểm tra dữ liệu hợp lệ (có thể mở rộng kiểm tra kiểu, không null, v.v.)
if (
    $name === null || $age === null || $occupation === null ||
    $month === null || $year === null ||
    $daysInMonth === null || $dailyCO2 === null || $monthlyCO2 === null
) {
    http_response_code(400);
    echo json_encode(["success" => false, "message" => "Missing required fields"]);
    exit();
}

// Import config kết nối DB
require_once "config.php";

// Dùng prepared statement để chống SQL injection
$sql = "INSERT INTO user_data 
    (name, age, occupation, month, year, days_in_month, daily_co2, monthly_co2)
    VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
$stmt = $conn->prepare($sql);
if (!$stmt) {
    http_response_code(500);
    echo json_encode(["success" => false, "message" => "Prepare failed: " . $conn->error]);
    exit();
}

// Gắn tham số. Giả sử:
// name, occupation là string
// age, month, year, daysInMonth là số nguyên
// dailyCO2, monthlyCO2 là số thực (float/double)
$stmt->bind_param(
    "sissiddi", 
    $name,         // s = string
    $age,          // i = integer
    $occupation,   // s = string
    $month,        // i
    $year,         // i
    $daysInMonth,  // i
    $dailyCO2,     // d = double / float
    $monthlyCO2    // d
);

// Thực thi
if ($stmt->execute()) {
    echo json_encode([
        "success" => true,
        "insertId" => $stmt->insert_id
    ]);
} else {
    http_response_code(500);
    echo json_encode([
        "success" => false,
        "message" => "Execute failed: " . $stmt->error
    ]);
}

// Đóng statement và kết nối
$stmt->close();
$conn->close();
?>
