package com.example.carbontracker.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.carbontracker.data.UserData
import com.example.carbontracker.data.UserDataState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DateSelectionScreen(onNextClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Chọn tháng kiểm tra",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(32.dp))

        OutlinedTextField(
            value = UserDataState.month,
            onValueChange = { UserDataState.month = it },
            label = { Text("Tháng (1-12)") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = UserDataState.year,
            onValueChange = { UserDataState.year = it },
            label = { Text("Năm") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Calculate days in month logic can be added here

        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = {
                // Validate and set daysInMonth
                UserDataState.daysInMonth = 30 // Placeholder, implement actual calculation
                onNextClick()
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            enabled = UserDataState.month.isNotEmpty() && UserDataState.year.isNotEmpty()
        ) {
            Text(
                text = "Tiếp tục",
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}