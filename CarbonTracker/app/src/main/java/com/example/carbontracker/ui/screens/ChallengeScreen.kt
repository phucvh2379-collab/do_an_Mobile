package com.example.carbontracker.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.carbontracker.data.UserDataState // Import đúng

data class Challenge(
    val day: Int,
    val title: String,
    val description: String,
    val icon: ImageVector,
    val co2Reduction: String,
    var completed: Boolean = false
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChallengeScreen(onBackClick: () -> Unit) {
    val challenges = remember {
        mutableStateListOf(
            Challenge(1, "Đi bộ thay vì xe máy", "Đi bộ hoặc đi xe đạp ít nhất 1 chuyến hôm nay", Icons.Default.DirectionsWalk, "0.5 kg CO₂"),
            Challenge(2, "Ăn chay 1 bữa", "Thay thế 1 bữa ăn thịt bằng thức ăn chay", Icons.Default.Eco, "0.8 kg CO₂"),
            Challenge(3, "Tắt điện không cần thiết", "Tắt tất cả thiết bị điện khi không sử dụng", Icons.Default.PowerOff, "0.3 kg CO₂"),
            Challenge(4, "Sử dụng túi vải", "Không sử dụng túi nilon khi mua sắm", Icons.Default.ShoppingBag, "0.2 kg CO₂"),
            Challenge(5, "Tiết kiệm nước", "Giảm thời gian tắm và tắt vòi khi không dùng", Icons.Default.Water, "0.4 kg CO₂"),
            Challenge(6, "Phân loại rác", "Phân loại rác thải đúng cách", Icons.Default.Recycling, "0.3 kg CO₂"),
            Challenge(7, "Chia sẻ kiến thức", "Chia sẻ thông tin về bảo vệ môi trường cho bạn bè", Icons.Default.Share, "Tác động lan tỏa")
        )
    }

    val completedChallenges = challenges.count { it.completed }
    val progressPercentage = completedChallenges.toFloat() / challenges.size

    LaunchedEffect(completedChallenges) {
        UserDataState.challengeProgress = completedChallenges  // FIXED HERE
    }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        // Top App Bar
        TopAppBar(
            title = { Text("Thử thách 7 ngày sống xanh") },
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
            modifier = Modifier.padding(24.dp)
        ) {
            // Progress Section
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
                    Text(
                        text = "Tiến độ thử thách",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Box(
                        modifier = Modifier.size(100.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(
                            progress = progressPercentage,
                            modifier = Modifier.fillMaxSize(),
                            strokeWidth = 8.dp,
                            color = Color(0xFF4CAF50)
                        )
                        Text(
                            text = "$completedChallenges/7",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF4CAF50)
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Hoàn thành ${(progressPercentage * 100).toInt()}%",
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    )

                    if (completedChallenges == challenges.size) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "🎉 Chúc mừng! Bạn đã hoàn thành thử thách!",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color(0xFF4CAF50)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Danh sách thử thách",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(16.dp))
        }

        // Challenges List
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(horizontal = 24.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            itemsIndexed(challenges) { index, challenge ->
                ChallengeItem(
                    challenge = challenge,
                    onToggleComplete = {
                        challenges[index] = challenge.copy(completed = !challenge.completed)
                    }
                )
            }
        }
    }
}

@Composable
fun ChallengeItem(
    challenge: Challenge,
    onToggleComplete: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = if (challenge.completed)
                Color(0xFFE8F5E8) else MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = if (challenge.completed) 2.dp else 4.dp
        )
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Day Circle
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(
                        if (challenge.completed)
                            Color(0xFF4CAF50) else MaterialTheme.colorScheme.primary
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "${challenge.day}",
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            // Challenge Content
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = challenge.title,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (challenge.completed)
                        Color(0xFF4CAF50) else MaterialTheme.colorScheme.onSurface
                )

                Text(
                    text = challenge.description,
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(top = 4.dp)
                ) {
                    Icon(
                        imageVector = challenge.icon,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Giảm ${challenge.co2Reduction}",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            // Complete Button
            if (challenge.completed) {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = "Completed",
                    tint = Color(0xFF4CAF50),
                    modifier = Modifier.size(32.dp)
                )
            } else {
                Button(
                    onClick = onToggleComplete,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    ),
                    modifier = Modifier.height(36.dp)
                ) {
                    Text(
                        text = "Hoàn thành",
                        fontSize = 12.sp
                    )
                }
            }
        }
    }
}
