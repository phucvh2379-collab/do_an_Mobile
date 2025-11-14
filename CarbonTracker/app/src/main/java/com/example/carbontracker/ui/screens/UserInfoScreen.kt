package com.example.carbontracker.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
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
fun UserInfoScreen(onNextClick: () -> Unit) {
    val occupations = listOf(
        "Học sinh", "Sinh viên", "Giáo viên", "Công chức",
        "Nhân viên văn phòng", "Công nhân", "Khác"
    )
    var selectedOccupation by remember { mutableStateOf(occupations[0]) }
    var expanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = Icons.Default.Person,
            contentDescription = "User Info",
            modifier = Modifier.size(80.dp),
            tint = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Thông tin cá nhân",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Name Input
        OutlinedTextField(
            value = UserDataState.name,
            onValueChange = { UserDataState.name = it },
            label = { Text("Tên (không bắt buộc)") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Age Input
        OutlinedTextField(
            value = UserDataState.age,
            onValueChange = { UserDataState.age = it },
            label = { Text("Tuổi") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Occupation Dropdown
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded }
        ) {
            OutlinedTextField(
                value = selectedOccupation,
                onValueChange = {},
                readOnly = true,
                label = { Text("Nghề nghiệp") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth()
            )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                occupations.forEach { occupation ->
                    DropdownMenuItem(
                        text = { Text(occupation) },
                        onClick = {
                            selectedOccupation = occupation
                            UserDataState.occupation = occupation
                            expanded = false
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = onNextClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            enabled = UserDataState.age.isNotEmpty()
        ) {
            Text(
                text = "Tiếp tục",
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}