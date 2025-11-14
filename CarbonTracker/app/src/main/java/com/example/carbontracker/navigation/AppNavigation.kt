package com.example.carbontracker.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.carbontracker.ui.screens.*

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "welcome"
    ) {
        composable("welcome") {
            WelcomeScreen(
                onStartClick = { navController.navigate("user_info") }
            )
        }

        composable("user_info") {
            UserInfoScreen(
                onNextClick = { navController.navigate("date_selection") }
            )
        }

        composable("date_selection") {
            DateSelectionScreen(
                onNextClick = { navController.navigate("survey") }
            )
        }

        composable("survey") {
            SurveyScreen(
                onCalculateClick = { navController.navigate("result") }
            )
        }

        composable("result") {
            ResultScreen(
                onChallengeClick = { navController.navigate("challenge") },
                onDashboardClick = { navController.navigate("dashboard") },
                onKnowledgeClick = { navController.navigate("knowledge") }
            )
        }

        composable("challenge") {
            ChallengeScreen(
                onBackClick = { navController.popBackStack() }
            )
        }

        composable("dashboard") {
            DashboardScreen(
                onBackClick = { navController.popBackStack() }
            )
        }

        composable("knowledge") {
            KnowledgeScreen(
                onBackClick = { navController.popBackStack() }
            )
        }
    }
}