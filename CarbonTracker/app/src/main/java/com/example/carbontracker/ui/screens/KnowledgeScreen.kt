package com.example.carbontracker.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class KnowledgeItem(
    val title: String,
    val description: String,
    val content: String,
    val icon: ImageVector,
    val color: Color,
    var expanded: Boolean = false
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun KnowledgeScreen(onBackClick: () -> Unit) {
    val knowledgeItems = remember {
        mutableStateListOf(
            KnowledgeItem(
                title = "Dấu chân carbon là gì?",
                description = "Tìm hiểu về khái niệm dấu chân carbon và tầm quan trọng của nó",
                content = """
                Dấu chân carbon (Carbon Footprint) là tổng lượng khí nhà kính được thải ra từ các hoạt động của con người, tính bằng đơn vị CO₂ tương đương.
                
                Các nguồn chính gây ra dấu chân carbon:
                • Đi lại và vận chuyển (xe máy, ô tô, máy bay)
                • Sử dụng năng lượng (điện, gas, nhiên liệu)
                • Ăn uống (chăn nuôi, trồng trọt, vận chuyển thực phẩm)
                • Tiêu dùng (sản xuất, vận chuyển hàng hóa)
                • Xử lý chất thải
                
                Việc theo dõi dấu chân carbon giúp chúng ta nhận thức được tác động của mình đến môi trường và tìm cách giảm thiểu.
                """.trimIndent(),
                icon = Icons.Default.Eco,
                color = Color(0xFF4CAF50)
            ),
            KnowledgeItem(
                title = "Tác hại của khí nhà kính",
                description = "Hiểu về hiệu ứng nhà kính và biến đổi khí hậu",
                content = """
                Khí nhà kính là những loại khí trong bầu khí quyển có khả năng hấp thụ và giữ nhiệt từ Mặt Trời.
                
                Các loại khí nhà kính chính:
                • CO₂ (Carbon dioxide) - 76% tổng lượng phát thải
                • CH₄ (Methane) - 16%
                • N₂O (Nitrous oxide) - 6%
                • Khí F (F-gases) - 2%
                
                Tác hại của khí nhà kính:
                • Làm tăng nhiệt độ toàn cầu
                • Tan băng ở hai cực, nước biển dâng cao
                • Thay đổi mô hình thời tiết
                • Hạn hán, lũ lụt, bão tố cực đoan
                • Ảnh hưởng đến nông nghiệp và an ninh lương thực
                • Suy giảm đa dạng sinh học
                """.trimIndent(),
                icon = Icons.Default.Warning,
                color = Color(0xFFF44336)
            ),
            KnowledgeItem(
                title = "Cách giảm phát thải đơn giản",
                description = "Những hành động nhỏ mà ai cũng có thể làm",
                content = """
                Trong cuộc sống hàng ngày, chúng ta có thể thực hiện nhiều hành động đơn giản để giảm phát thải:
                
                🚶 Đi lại:
                • Đi bộ, đi xe đạp cho quãng đường ngắn
                • Sử dụng phương tiện công cộng
                • Chia sẻ xe với bạn bè, đồng nghiệp
                • Làm việc tại nhà khi có thể
                
                🍃 Ăn uống:
                • Ăn chay 1-2 ngày/tuần
                • Mua thực phẩm địa phương, theo mùa
                • Giảm lãng phí thực phẩm
                • Hạn chế đồ ăn nhanh, chế biến sẵn
                
                ⚡ Năng lượng:
                • Tắt điện khi không sử dụng
                • Sử dụng bóng đèn LED
                • Điều chỉnh nhiệt độ điều hòa hợp lý
                • Sử dụng thiết bị tiết kiệm năng lượng
                
                ♻️ Tiêu dùng:
                • Mua ít hơn, sử dụng lâu hơn
                • Tái sử dụng và tái chế
                • Chọn sản phẩm thân thiện môi trường
                • Phân loại rác đúng cách
                """.trimIndent(),
                icon = Icons.Default.Lightbulb,
                color = Color(0xFF2196F3)
            ),
            KnowledgeItem(
                title = "Năng lượng tái tạo",
                description = "Tìm hiểu về các nguồn năng lượng sạch",
                content = """
                Năng lượng tái tạo là nguồn năng lượng có thể được bổ sung tự nhiên và không cạn kiệt.
                
                Các loại năng lượng tái tạo:
                
                ☀️ Năng lượng mặt trời:
                • Chuyển đổi ánh sáng mặt trời thành điện
                • Không gây ô nhiễm
                • Chi phí giảm dần theo thời gian
                
                💨 Năng lượng gió:
                • Sử dụng gió để quay turbine tạo điện
                • Phù hợp với vùng có gió mạnh
                • Không gây phát thải khí nhà kính
                
                💧 Năng lượng thủy điện:
                • Sử dụng dòng chảy của nước
                • Ổn định và đáng tin cậy
                • Có thể lưu trữ năng lượng
                
                🔋 Năng lượng sinh khối:
                • Từ chất thải hữu cơ
                • Giúp xử lý rác thải
                • Giảm phụ thuộc vào nhiên liệu hóa thạch
                
                Việt Nam đang phát triển mạnh năng lượng tái tạo, đặc biệt là điện mặt trời và điện gió.
                """.trimIndent(),
                icon = Icons.Default.ElectricBolt,
                color = Color(0xFFFF9800)
            ),
            KnowledgeItem(
                title = "Kinh tế tuần hoàn",
                description = "Mô hình kinh tế bền vững cho tương lai",
                content = """
                Kinh tế tuần hoàn là mô hình kinh tế nhằm giảm thiểu chất thải và tối đa hóa việc sử dụng tài nguyên.
                
                Nguyên tắc 3R:
                
                🔄 Reduce (Giảm thiểu):
                • Giảm tiêu thụ không cần thiết
                • Chọn sản phẩm bền vững
                • Sử dụng ít tài nguyên hơn
                
                ♻️ Reuse (Tái sử dụng):
                • Sử dụng lại đồ vật cũ
                • Chia sẻ, cho mượn thay vì mua mới
                • Sửa chữa thay vì vứt bỏ
                
                🔁 Recycle (Tái chế):
                • Phân loại rác đúng cách
                • Chọn sản phẩm từ vật liệu tái chế
                • Tham gia các chương trình thu hồi
                
                Lợi ích của kinh tế tuần hoàn:
                • Giảm ô nhiễm môi trường
                • Tiết kiệm tài nguyên thiên nhiên
                • Tạo việc làm mới
                • Giảm chi phí sản xuất
                • Phát triển bền vững
                """.trimIndent(),
                icon = Icons.Default.Recycling,
                color = Color(0xFF9C27B0)
            ),
            KnowledgeItem(
                title = "Bảo vệ đa dạng sinh học",
                description = "Tầm quan trọng của việc bảo tồn thiên nhiên",
                content = """
                Đa dạng sinh học là sự phong phú của các loài sinh vật và hệ sinh thái trên Trái Đất.
                
                Tại sao đa dạng sinh học quan trọng?
                
                🌱 Cung cấp dịch vụ hệ sinh thái:
                • Làm sạch không khí và nước
                • Thụ phên cho cây trồng
                • Điều hòa khí hậu
                • Kiểm soát sâu bệnh tự nhiên
                
                💊 Y học và dược phẩm:
                • Nhiều loại thuốc từ thực vật
                • Nghiên cứu gen để chữa bệnh
                • Phát triển vắc-xin mới
                
                🍎 An ninh lương thực:
                • Đa dạng giống cây trồng
                • Kháng được sâu bệnh
                • Thích ứng biến đổi khí hậu
                
                Cách bảo vệ đa dạng sinh học:
                • Không mua bán động vật hoang dã
                • Trồng cây bản địa
                • Giảm sử dụng thuốc trừ sâu
                • Bảo vệ môi trường sống tự nhiên
                • Tham gia các chương trình bảo tồn
                
                Việt Nam có hơn 16.000 loài thực vật và 15.000 loài động vật, cần được bảo vệ.
                """.trimIndent(),
                icon = Icons.Default.Forest,
                color = Color(0xFF4CAF50)
            )
        )
    }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        // Top App Bar
        TopAppBar(
            title = { Text("Kiến thức môi trường") },
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

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                // Header
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFFE8F5E8)
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Default.School,
                            contentDescription = "Knowledge",
                            modifier = Modifier.size(48.dp),
                            tint = Color(0xFF4CAF50)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Cùng nhau học hỏi để bảo vệ môi trường",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium,
                            textAlign = TextAlign.Center,
                            color = Color(0xFF4CAF50)
                        )
                    }
                }
            }

            items(knowledgeItems) { item ->
                KnowledgeCard(
                    item = item,
                    onExpandClick = {
                        val index = knowledgeItems.indexOf(item)
                        knowledgeItems[index] = item.copy(expanded = !item.expanded)
                    }
                )
            }

            item {
                // Footer
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFFF3E5F5)
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "💡 Mẹo hay",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF9C27B0)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Hãy chia sẻ những kiến thức này với bạn bè và gia đình để cùng nhau bảo vệ môi trường!",
                            fontSize = 14.sp,
                            textAlign = TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Button(
                            onClick = { /* TODO: Implement share */ },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF9C27B0)
                            )
                        ) {
                            Icon(
                                imageVector = Icons.Default.Share,
                                contentDescription = "Share"
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Chia sẻ kiến thức")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun KnowledgeCard(
    item: KnowledgeItem,
    onExpandClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (item.expanded) item.color.copy(alpha = 0.05f) else MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(item.color.copy(alpha = 0.1f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = item.title,
                        tint = item.color,
                        modifier = Modifier.size(24.dp)
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = item.title,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = item.color
                    )
                    Text(
                        text = item.description,
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    )
                }

                IconButton(onClick = onExpandClick) {
                    Icon(
                        imageVector = if (item.expanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                        contentDescription = if (item.expanded) "Thu gọn" else "Mở rộng",
                        tint = item.color
                    )
                }
            }

            if (item.expanded) {
                Spacer(modifier = Modifier.height(12.dp))
                Divider(color = item.color.copy(alpha = 0.3f))
                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = item.content,
                    fontSize = 14.sp,
                    lineHeight = 20.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}