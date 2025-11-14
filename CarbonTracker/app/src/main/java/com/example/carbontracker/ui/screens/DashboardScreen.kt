package com.example.carbontracker.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import co.yml.charts.axis.AxisData
import co.yml.charts.common.model.Point
import co.yml.charts.ui.barchart.BarChart
import co.yml.charts.ui.barchart.models.BarChartData
import co.yml.charts.ui.barchart.models.BarData
import co.yml.charts.ui.linechart.LineChart
import co.yml.charts.ui.linechart.model.Line
import co.yml.charts.ui.linechart.model.LineChartData
import co.yml.charts.ui.linechart.model.LinePlotData
import com.example.carbontracker.data.UserData
import com.example.carbontracker.data.UserDataState
import kotlin.random.Random

// Hàm xác định màu sắc dựa trên mức độ nghiêm trọng của CO2 (kg) - FIX: Thay Float thành Double để match với dailyCO2
// - Xanh lá: < 3kg (thấp, tốt)
// - Vàng: 3-5kg (trung bình)
// - Đỏ: >5kg (cao, nghiêm trọng)
fun getSeverityColor(co2: Double): Color {  // Đổi từ Float sang Double
    return when {
        co2 < 3.0 -> Color(0xFF4CAF50) // Xanh lá - thấp nhất
        co2 <= 5.0 -> Color(0xFFFF9800) // Vàng - trung bình
        else -> Color(0xFFF44336) // Đỏ - cao
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(onBackClick: () -> Unit) {
    val scrollState = rememberScrollState()

    val weeklyData = remember {
        if (UserDataState.co2History.isEmpty()) {
            List(7) { Random.nextFloat() * 5f + 2f }
        } else {
            UserDataState.co2History.takeLast(7).ifEmpty {
                List(7) { Random.nextFloat() * 5f + 2f }
            }
        }
    }

    val monthlyData = remember {
        List(30) { Random.nextFloat() * 6f + 1f }
    }

    // Tính tổng weekly CO2 để xác định màu
    val weeklyTotal = weeklyData.sum()

    Column(modifier = Modifier.fillMaxSize()) {
        TopAppBar(
            title = { Text("Báo cáo & Biểu đồ") },
            navigationIcon = {
                IconButton(onClick = onBackClick) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.primary,
                titleContentColor = Color.White,
                navigationIconContentColor = Color.White
            )
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Màu động cho Hôm nay dựa trên dailyCO2 (bây giờ match Double)
                val dailyColor = getSeverityColor(UserDataState.dailyCO2)
                SummaryCard("Hôm nay", "${"%.1f".format(UserDataState.dailyCO2)} kg", Icons.Default.Today, dailyColor, Modifier.weight(1f))

                // Màu động cho Tuần này dựa trên weeklyTotal (sum() trả Float, nhưng /7 thành Double, match hàm)
                val weeklyAvg = weeklyTotal / 7f  // Ép về Float trước khi chia để tránh Double, nhưng hàm giờ nhận Double nên ok
                val weeklyColor = getSeverityColor(weeklyAvg.toDouble())
                SummaryCard("Tuần này", "${"%.1f".format(weeklyTotal)} kg", Icons.Default.CalendarMonth, weeklyColor, Modifier.weight(1f))

                // Màu cho Thử thách: Xanh nếu hoàn thành, Vàng nếu trung bình, Đỏ nếu thấp
                val challengeColor = when {
                    UserDataState.challengeProgress >= 6 -> Color(0xFF4CAF50)
                    UserDataState.challengeProgress >= 3 -> Color(0xFFFF9800)
                    else -> Color(0xFFF44336)
                }
                SummaryCard("Thử thách", "${UserDataState.challengeProgress}/7", Icons.Default.EmojiEvents, challengeColor, Modifier.weight(1f))
            }

            Spacer(modifier = Modifier.height(24.dp))

            ChartCard("Xu hướng 7 ngày qua", "Lượng CO₂ phát thải hàng ngày") {
                WeeklyLineChart(weeklyData)
            }

            Spacer(modifier = Modifier.height(16.dp))

            ChartCard("Báo cáo tháng", "So sánh phát thải theo tuần") {
                MonthlyBarChart(monthlyData)
            }

            Spacer(modifier = Modifier.height(16.dp))

            ComparisonCard()
            Spacer(modifier = Modifier.height(16.dp))
            EnvironmentalImpactCard()
        }
    }
}

@Composable
fun SummaryCard(title: String, value: String, icon: androidx.compose.ui.graphics.vector.ImageVector, color: Color, modifier: Modifier = Modifier) {
    Card(modifier = modifier, colors = CardDefaults.cardColors(containerColor = color.copy(alpha = 0.1f))) {
        Column(modifier = Modifier.padding(12.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(icon, contentDescription = title, tint = color, modifier = Modifier.size(24.dp))
            Spacer(modifier = Modifier.height(4.dp))
            Text(title, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f))
            Text(value, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = color)
        }
    }
}

@Composable
fun ChartCard(title: String, description: String, content: @Composable () -> Unit) {
    Card(modifier = Modifier.fillMaxWidth(), elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(title, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
            Text(description, fontSize = 14.sp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f))
            Spacer(modifier = Modifier.height(16.dp))
            content()
        }
    }
}

@Composable
fun WeeklyLineChart(data: List<Float>) {
    val points = data.mapIndexed { i, v -> Point(i.toFloat(), v) }
    val lineChartData = LineChartData(
        linePlotData = LinePlotData(lines = listOf(Line(dataPoints = points))),
        xAxisData = AxisData.Builder()
            .axisStepSize(50.dp)
            .steps(data.size - 1)
            .labelData { i -> listOf("CN","T2","T3","T4","T5","T6","T7").getOrNull(i) ?: "" }
            .build(),
        yAxisData = AxisData.Builder()
            .steps(5)
            .labelAndAxisLinePadding(20.dp)
            .labelData { i -> "${i * 2}" }
            .build()
    )
    LineChart(modifier = Modifier.fillMaxWidth().height(200.dp), lineChartData = lineChartData)
}

@Composable
fun MonthlyBarChart(data: List<Float>) {
    val weeks = data.chunked(7).map { it.average().toFloat() }
    // Màu động cho từng bar dựa trên mức độ nghiêm trọng (average() trả Double, ép toFloat() rồi toDouble() cho hàm)
    val bars = weeks.mapIndexed { i, v ->
        BarData(
            point = Point(i.toFloat(), v),
            color = getSeverityColor(v.toDouble())  // FIX: Ép sang Double để match hàm
        )
    }
    val barChartData = BarChartData(
        chartData = bars,
        xAxisData = AxisData.Builder()
            .axisStepSize(50.dp)
            .steps(weeks.size - 1)
            .labelData { i -> "Tuần ${i + 1}" }
            .build(),
        yAxisData = AxisData.Builder()
            .steps(5)
            .labelAndAxisLinePadding(20.dp)
            .labelData { i -> "${i * 2}" }
            .build()
    )
    BarChart(modifier = Modifier.fillMaxWidth().height(200.dp), barChartData = barChartData)
}

@Composable
fun ComparisonCard() {
    // Màu động cho user dựa trên dailyCO2 (Double)
    val userColor = getSeverityColor(UserDataState.dailyCO2)
    val avgVNColor = getSeverityColor(4.2)  // 4.2 là Double literal
    val worldColor = getSeverityColor(4.8)

    Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = userColor.copy(alpha = 0.1f))) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Compare, contentDescription = null, tint = userColor, modifier = Modifier.size(24.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text("So sánh với cộng đồng", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = userColor)
            }
            Spacer(modifier = Modifier.height(12.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                ComparisonItem("Bạn", UserDataState.dailyCO2, userColor)  // FIX: Truyền Double trực tiếp
                ComparisonItem("Trung bình VN", 4.2, avgVNColor)
                ComparisonItem("Thế giới", 4.8, worldColor)
            }
        }
    }
}

@Composable
fun ComparisonItem(label: String, value: Double, color: Color) {  // FIX: Thay Float thành Double cho value
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier.width(60.dp).height((value * 20).dp).background(color = color, shape = RoundedCornerShape(topStart = 4.dp, topEnd = 4.dp))
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text("${"%.1f".format(value)} kg", fontSize = 12.sp, fontWeight = FontWeight.Medium, color = color)
        Text(label, fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f))
    }
}

@Composable
fun EnvironmentalImpactCard() {
    val dailyCO2 = UserDataState.dailyCO2
    val saved = 4.0 - dailyCO2
    val impactColor = getSeverityColor(dailyCO2)  // Bây giờ match Double

    Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = impactColor.copy(alpha = 0.1f))) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Eco, contentDescription = null, tint = impactColor, modifier = Modifier.size(24.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text("Tác động môi trường", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = impactColor)
            }
            Spacer(modifier = Modifier.height(12.dp))
            if (saved > 0) {
                Text("🌱 Bạn đã tiết kiệm ${"%.1f".format(saved)} kg CO₂ so với mức trung bình!", fontSize = 14.sp, fontWeight = FontWeight.Medium, color = Color(0xFF4CAF50))
                Spacer(modifier = Modifier.height(8.dp))
                Text("Tương đương với:", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f))
                Spacer(modifier = Modifier.height(4.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("🌳 ${"%.1f".format(saved * 0.5)} cây xanh", fontSize = 12.sp)
                    Text("⚡ ${"%.0f".format(saved * 100)} kWh điện", fontSize = 12.sp)
                    Text("🚗 ${"%.0f".format(saved * 4)} km lái xe", fontSize = 12.sp)
                }
            } else {
                Text("Hãy cố gắng giảm thiểu phát thải để bảo vệ môi trường tốt hơn!", fontSize = 14.sp, color = impactColor)
            }
        }
    }
}