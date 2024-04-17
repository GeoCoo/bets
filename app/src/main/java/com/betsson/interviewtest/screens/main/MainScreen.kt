package com.betsson.interviewtest.screens.main

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.betsson.interviewtest.model.NavRoutes
import com.betsson.interviewtest.screens.bets.BetsScreen
import com.betsson.interviewtest.screens.single.SingleScreen

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@SuppressLint("UnusedMaterialScaffoldPaddingParameter", "UnusedMaterial3ScaffoldPaddingParameter")

@Composable
fun MainScreen() {
    val navController = rememberNavController()

    Scaffold {
        NavHost(
            navController = navController,
            startDestination = NavRoutes.BETS.route
        ) {
            composable(route = NavRoutes.BETS.route) {
                BetsScreen(navController)
            }
            composable(
                route = NavRoutes.BET.route,
                arguments = listOf(navArgument("type") {
                    type = NavType.StringType
                })
            ) { backStackEntry ->
                SingleScreen(navController, backStackEntry.arguments?.getString("type") ?: "")
            }
        }
    }
}
