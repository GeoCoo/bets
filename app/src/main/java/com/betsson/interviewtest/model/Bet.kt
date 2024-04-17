package com.betsson.interviewtest.model

//open class Bet(var type: String, var sellIn: Int, var odds: Int, var image: String) {
//    override fun toString(): String {
//        return this.type + ", " + this.sellIn + ", " + this.odds
//    }
//}

data class Bet(var type: String, var sellIn: Int, var odds: Int, var image: String)

enum class NavRoutes(val route: String) {
    BETS("/bets"), BET("/bet/{type}")
}