package com.betsson.interviewtest

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.betson.interviewTest.feature_bets_screen.router.betsRoute
import com.betson.interviewTest.feature_bets_screen.router.betsScreen

@Composable
fun MainNavigation(
    navController: NavHostController = rememberNavController(),
) {
    NavHost(navController = navController, startDestination = betsRoute) {
        betsScreen()
    }
}