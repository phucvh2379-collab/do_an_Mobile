package com.example.carbontracker.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.carbontracker.data.CarbonCalculator
import com.example.carbontracker.data.UserData
import com.example.carbontracker.data.UserDataState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SurveyScreen(onCalculateClick: () -> Unit) {
    val scrollState = rememberScrollState()
    val transportModes = listOf(
    "Ô tô",
    "Xe máy",
    "Xe buýt",
    "Ô tô điện",
    "Xe đạp",
    "Đi bộ",
    "Xe tải",
    "Máy bay"
)
    var selectedModes by remember { mutableStateOf(setOf<String>()) }
    var modeData by remember { mutableStateOf(mapOf<String, Pair<String, String>>()) } // mode -> (km, days)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(24.dp)
    ) {
        Text(
            text = "Khảo sát hành vi hàng ngày",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Transportation Section
        SectionCard(
            title = "Đi lại",
            icon = Icons.Default.DirectionsCar
        ) {
            transportModes.forEach { mode ->
                var km by remember { mutableStateOf("") }
                var days by remember { mutableStateOf("") }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(
                        checked = selectedModes.contains(mode),
                        onCheckedChange = {
                            if (it) selectedModes += mode else selectedModes -= mode
                        }
                    )
                    Text(mode)
                }
                if (selectedModes.contains(mode)) {
                    OutlinedTextField(
                        value = km,
                        onValueChange = { km = it },
                        label = { Text("KM") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                    )
                    OutlinedTextField(
                        value = days,
                        onValueChange = { days = it },
                        label = { Text("Ngày") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                    )
                    modeData = modeData + (mode to (km to days))
                }
            }
            // Validate days <= daysInMonth
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Food Section
        SectionCard(
            title = "Ăn uống",
            icon = Icons.Default.Restaurant
        ) {
            CheckboxItem(
                text = "Ăn chay trường",
                checked = UserDataState.isVegetarianFull,
                onCheckedChange = { UserDataState.isVegetarianFull = it }
            )

            if (!UserDataState.isVegetarianFull) {
                OutlinedTextField(
                    value = UserDataState.vegetarianDays.toString(),
                    onValueChange = { UserDataState.vegetarianDays = it.toIntOrNull() ?: 0 },
                    label = { Text("Số ngày ăn chay") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
            }

            CheckboxItem(
                text = "Ăn thực phẩm chế biến sẵn",
                checked = UserDataState.processedFood,
                onCheckedChange = { UserDataState.processedFood = it }
            )
            if (UserDataState.processedFood) {
                OutlinedTextField(
                    value = UserDataState.processedFoodDays.toString(),
                    onValueChange = { UserDataState.processedFoodDays = it.toIntOrNull() ?: 0 },
                    label = { Text("Số ngày") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
            }

            OutlinedTextField(
                value = UserDataState.redMeatDays.toString(),
                onValueChange = { UserDataState.redMeatDays = it.toIntOrNull() ?: 0 },
                label = { Text("Số ngày ăn thịt đỏ") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Electricity Section
        SectionCard(
            title = "Sử dụng điện",
            icon = Icons.Default.Bolt
        ) {
            OutlinedTextField(
                value = UserDataState.electricityBill.toString(),
                onValueChange = { UserDataState.electricityBill = it.toDoubleOrNull() ?: 0.0 },
                label = { Text("Số tiền điện (VND)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )

            OutlinedTextField(
                value = UserDataState.familySize.toString(),
                onValueChange = { UserDataState.familySize = it.toIntOrNull() ?: 1 },
                label = { Text("Số người trong gia đình") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )

            LaunchedEffect(UserDataState.electricityBill, UserDataState.familySize) {
                UserDataState.totalKWh = CarbonCalculator.calculateKWhFromBill(UserDataState.electricityBill, UserDataState.familySize)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Consumption Section
        SectionCard(
            title = "Vật dụng",
            icon = Icons.Default.ShoppingCart
        ) {
            CheckboxItem(
                text = "Sử dụng túi tái sử dụng, bình nước cá nhân",
                checked = UserDataState.useReusableBagBottle,
                onCheckedChange = { UserDataState.useReusableBagBottle = it }
            )

            OutlinedTextField(
                value = UserDataState.phoneHours.toString(),
                onValueChange = { UserDataState.phoneHours = it.toDoubleOrNull() ?: 0.0 },
                label = { Text("Giờ sử dụng điện thoại") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )

            OutlinedTextField(
                value = UserDataState.laptopHours.toString(),
                onValueChange = { UserDataState.laptopHours = it.toDoubleOrNull() ?: 0.0 },
                label = { Text("Giờ sử dụng máy tính") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )

            OutlinedTextField(
                value = UserDataState.gasUsesPerDay.toString(),
                onValueChange = { UserDataState.gasUsesPerDay = it.toIntOrNull() ?: 0 },
                label = { Text("Số lần dùng bếp gas/ngày") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )

            OutlinedTextField(
                value = UserDataState.laundryInterval.toString(),
                onValueChange = { UserDataState.laundryInterval = it.toIntOrNull() ?: 1 },
                label = { Text("Khoảng cách giặt đồ (ngày)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = {
                UserDataState.transportOptions = modeData.mapValues { (it.value.first.toDoubleOrNull() ?: 0.0) to (it.value.second.toIntOrNull() ?: 0) }
                onCalculateClick()
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary
            )
        ) {
            Text(
                text = "Tính toán CO₂",
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
fun SectionCard(
    title: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = title,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            content()
        }
    }
}

@Composable
fun CheckboxItem(
    text: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        Checkbox(
            checked = checked,
            onCheckedChange = onCheckedChange
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = text)
    }
}