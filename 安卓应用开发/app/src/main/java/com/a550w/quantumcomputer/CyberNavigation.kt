package com.a550w.quantumcomputer

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument

@Composable
fun CyberNavigation(
    navController: NavHostController = rememberNavController()
) {
    var currentExpression by remember { mutableStateOf("") }

    NavHost(
        navController = navController,
        startDestination = Screen.Splash.route
    ) {
        composable(Screen.Splash.route) {
            SplashScreen(
                onNavigateToCalculator = {
                    navController.navigate(Screen.Calculator.route) {
                        popUpTo(Screen.Splash.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.Calculator.route) {
            CalculatorScreen(
                onCalculate = { expression ->
                    currentExpression = expression
                    navController.navigate(Screen.Computing.route)
                }
            )
        }

        composable(Screen.Computing.route) {
            ComputingScreen(
                expression = currentExpression,
                onNavigateToHandCounting = { result ->
                    navController.navigate(Screen.HandCounting.createRoute(result)) {
                        popUpTo(Screen.Computing.route) { inclusive = true }
                    }
                },
                onNavigateToError = { message ->
                    navController.navigate(Screen.Error.createRoute(message)) {
                        popUpTo(Screen.Computing.route) { inclusive = true }
                    }
                }
            )
        }

        composable(
            route = Screen.Error.route,
            arguments = listOf(navArgument("message") { type = NavType.StringType })
        ) { backStackEntry ->
            val message = backStackEntry.arguments?.getString("message") ?: "未知错误"
            ErrorScreen(
                message = message,
                onNavigateBack = {
                    navController.navigate(Screen.Calculator.route) {
                        popUpTo(Screen.Calculator.route) { inclusive = true }
                    }
                }
            )
        }

        composable(
            route = Screen.HandCounting.route,
            arguments = listOf(navArgument("result") { type = NavType.IntType })
        ) { backStackEntry ->
            val result = backStackEntry.arguments?.getInt("result") ?: 0
            HandCountingScreen(
                result = result,
                expression = currentExpression,
                onComplete = {
                    currentExpression = ""
                    navController.navigate(Screen.Calculator.route) {
                        popUpTo(Screen.Calculator.route) { inclusive = true }
                    }
                }
            )
        }
    }
}
