package com.example.carbontracker.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.carbontracker.data.CarbonCalculator
import com.example.carbontracker.data.DBHelper
import com.example.carbontracker.data.UserData
import com.example.carbontracker.data.UserDataState
import androidx.compose.ui.platform.LocalContext
import android.widget.Toast

@Composable
fun ResultScreen(
    onChallengeClick: () -> Unit,
    onDashboardClick: () -> Unit,
    onKnowledgeClick: () -> Unit
) {
    val scrollState = rememberScrollState()
    val context = LocalContext.current

    // Tính toán CO2 dựa trên dữ liệu từ UserDataState
    val monthlyCO2 = CarbonCalculator.calculateMonthlyCO2(
        transportOptions = UserDataState.transportOptions,
        isVegetarianFull = UserDataState.isVegetarianFull,
        vegetarianDays = UserDataState.vegetarianDays,
        processedFood = UserDataState.processedFood,
        processedFoodDays = UserDataState.processedFoodDays,
        redMeatDays = UserDataState.redMeatDays,
        totalKWh = UserDataState.totalKWh,
        useReusableBagBottle = UserDataState.useReusableBagBottle,
        phoneHours = UserDataState.phoneHours,
        laptopHours = UserDataState.laptopHours,
        gasUsesPerDay = UserDataState.gasUsesPerDay,
        laundryInterval = UserDataState.laundryInterval,
        daysInMonth = UserDataState.daysInMonth,
        acHours = UserDataState.acHours
    )
    val dailyCO2 = CarbonCalculator.calculateDailyCO2(monthlyCO2, UserDataState.daysInMonth)
    val emissionLevel = CarbonCalculator.getEmissionLevel(dailyCO2)
    val recommendations = CarbonCalculator.getRecommendations(
        co2 = dailyCO2,
        transportOptions = UserDataState.transportOptions,
        isVegetarianFull = UserDataState.isVegetarianFull,
        useReusableBagBottle = UserDataState.useReusableBagBottle,
        acHours = UserDataState.acHours
    )
    val averageCO2 = 4.0 // Trung bình người Việt Nam

    // Cập nhật trạng thái khi dailyCO2 thay đổi
    LaunchedEffect(dailyCO2) {
        UserDataState.dailyCO2 = dailyCO2
        UserDataState.monthlyCO2 = monthlyCO2
        UserDataState.co2History = UserDataState.co2History + dailyCO2.toFloat()
        DBHelper().saveUserData { success, message ->
            if (success) {
                Toast.makeText(context, "Dữ liệu đã được lưu thành công", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, "Lỗi khi lưu dữ liệu: $message", Toast.LENGTH_LONG).show()
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Kết quả phân tích",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(24.dp))

        // CO2 Result Circle
        Box(
            modifier = Modifier
                .size(200.dp)
                .clip(CircleShape)
                .background(
                    when (emissionLevel) {
                        "Thấp" -> Color(0xFF4CAF50)
                        "Trung bình" -> Color(0xFFFF9800)
                        else -> Color(0xFFF44336)
                    }
                ),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = String.format("%.1f", dailyCO2),
                    fontSize = 36.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Text(
                    text = "kg CO₂",
                    fontSize = 16.sp,
                    color = Color.White
                )
                Text(
                    text = "hôm nay",
                    fontSize = 14.sp,
                    color = Color.White.copy(alpha = 0.8f)
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Comparison with Average
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "So sánh với mức trung bình",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Trung bình người Việt Nam: ${averageCO2} kg CO₂/ngày",
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )

                val difference = dailyCO2 - averageCO2
                val comparisonText = if (difference > 0) {
                    "Cao hơn ${String.format("%.1f", difference)} kg"
                } else {
                    "Thấp hơn ${String.format("%.1f", -difference)} kg"
                }

                Text(
                    text = comparisonText,
                    fontSize = 14.sp,
                    color = if (difference > 0) Color(0xFFF44336) else Color(0xFF4CAF50),
                    fontWeight = FontWeight.Medium
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Emission Level
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = when (emissionLevel) {
                    "Thấp" -> Color(0xFFE8F5E8)
                    "Trung bình" -> Color(0xFFFFF3E0)
                    else -> Color(0xFFFFEBEE)
                }
            )
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = when (emissionLevel) {
                        "Thấp" -> Icons.Default.Eco
                        "Trung bình" -> Icons.Default.Warning
                        else -> Icons.Default.Error
                    },
                    contentDescription = emissionLevel,
                    tint = when (emissionLevel) {
                        "Thấp" -> Color(0xFF4CAF50)
                        "Trung bình" -> Color(0xFFFF9800)
                        else -> Color(0xFFF44336)
                    },
                    modifier = Modifier.size(32.dp)
                )

                Spacer(modifier = Modifier.width(12.dp))

                Column {
                    Text(
                        text = "Mức phát thải: $emissionLevel",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )

                    val levelDescription = when (emissionLevel) {
                        "Thấp" -> "Xuất sắc! Bạn đang sống rất thân thiện với môi trường"
                        "Trung bình" -> "Tốt! Bạn có thể cải thiện thêm một chút"
                        else -> "Cần cải thiện để bảo vệ môi trường tốt hơn"
                    }

                    Text(
                        text = levelDescription,
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    )
                }
            }
        }

        if (recommendations.isNotEmpty()) {
            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Gợi ý cải thiện",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(12.dp))

            recommendations.forEach { recommendation ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFFE3F2FD)
                    )
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Lightbulb,
                            contentDescription = "Tip",
                            tint = Color(0xFF2196F3),
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = recommendation,
                            fontSize = 14.sp
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Action Buttons
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(
                onClick = onChallengeClick,
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF4CAF50)
                )
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Default.EmojiEvents,
                        contentDescription = "Challenge"
                    )
                    Text("Thử thách", fontSize = 12.sp)
                }
            }

            Button(
                onClick = onDashboardClick,
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF2196F3)
                )
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Default.Dashboard,
                        contentDescription = "Dashboard"
                    )
                    Text("Báo cáo", fontSize = 12.sp)
                }
            }

            Button(
                onClick = onKnowledgeClick,
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFFF9800)
                )
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Default.School,
                        contentDescription = "Knowledge"
                    )
                    Text("Kiến thức", fontSize = 12.sp)
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Share Button
        OutlinedButton(
            onClick = {
                // TODO: Implement share functionality
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(
                imageVector = Icons.Default.Share,
                contentDescription = "Share"
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text("Chia sẻ kết quả")
        }
    }
}