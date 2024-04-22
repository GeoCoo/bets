package com.betson.interviewTest.core_tests.helpers

import com.betson.interviewTest.core_model.common.Bet


fun getItemsFromNetwork() = listOf(
    Bet("Winning team", 10, 20, "https://i.imgur.com/mx66SBD.jpeg"),
    Bet("Total score", 2, 0, "https://i.imgur.com/VnPRqcv.jpeg"),
    Bet("Player performance", 5, 7, "https://i.imgur.com/Urpc00H.jpeg"),
    Bet("First goal scorer", 0, 80, "https://i.imgur.com/Wy94Tt7.jpeg"),
    Bet("Number of fouls", 5, 49, "https://i.imgur.com/NMLpcKj.jpeg"),
    Bet("Corner kicks", 3, 6, "https://i.imgur.com/TiJ8y5l.jpeg")
)