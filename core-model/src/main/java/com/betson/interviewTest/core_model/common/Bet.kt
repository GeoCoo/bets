package com.betson.interviewTest.core_model.common

//open class Bet(var type: String, var sellIn: Int, var odds: Int, var image: String) {
//    override fun toString(): String {
//        return this.type + ", " + this.sellIn + ", " + this.odds
//    }
//}

data class Bet(var type: String, var sellIn: Int, var odds: Int, var image: String)

object Route{
    const val BetsScreen = "BetsScreen"
}