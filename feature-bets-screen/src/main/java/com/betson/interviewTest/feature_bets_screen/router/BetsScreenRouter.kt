package com.betson.interviewTest.feature_bets_screen.router

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.betson.interviewTest.feature_bets_screen.ui.BetsScreen


const val betsRoute = "bets"

fun NavGraphBuilder.betsScreen() {
    composable(betsRoute) {
        BetsScreen()
    }
}