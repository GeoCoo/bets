package com.betsson.interviewtest.screens.main

import android.location.Location
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.android.sdsc.base.MviViewModel
import com.android.sdsc.base.ViewEvent
import com.android.sdsc.base.ViewSideEffect
import com.android.sdsc.base.ViewState
import com.betsson.interviewtest.model.Bet
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject


data class State(
    var isLoading: Boolean = false, val bets: List<Bet>? = listOf(),
    val errorMessage: String? = ""
) : ViewState

sealed class Event : ViewEvent {
    data object GetInitBets : Event()
    data class UpdateOdds(val bets: List<Bet>?) : Event()
}

sealed class Effect : ViewSideEffect {}

@HiltViewModel
class MainViewModel @Inject constructor(
    private val mainInteractor: MainInteractor
) : MviViewModel<Event, State, Effect>() {
    override fun setInitialState(): State = State(
        isLoading = true
    )

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun handleEvents(event: Event) {
        when (event) {
            is Event.GetInitBets -> {
                viewModelScope.launch {
                    mainInteractor.getBets().collect {
                        when (it) {
                            is BetsPartialState.Failed -> {
                                setState {
                                    copy(
                                        isLoading = false,
                                        bets = null,
                                        errorMessage = it.errorMessage

                                    )
                                }
                            }

                            is BetsPartialState.Success -> {

                                setState {
                                    copy(
                                        isLoading = false,
                                        bets = it.bets
                                    )
                                }
                            }
                        }

                    }


                }
            }

            is Event.UpdateOdds -> {
                setState {
                    copy(
                        isLoading = false,
                        bets = calculateOdds(event.bets)
                    )
                }
            }
        }
    }

}

fun calculateOdds(bets: List<Bet>?): List<Bet> {
    val transformedBets = mutableListOf<Bet>()
    bets?.forEachIndexed { _, bet ->
        val updatedOdds = when {
            bet.type != "Total score" && bet.type != "Number of fouls" && bet.odds > 0 && bet.type != "First goal scorer" -> bet.odds - 1
            bet.type == "Total score" || bet.type == "Number of fouls" -> {
                if (bet.odds < 50) {
                    var updatedOdds = bet.odds + 1
                    if (bet.type == "Number of fouls") {
                        if (bet.sellIn < 11) {
                            updatedOdds++
                        }
                        if (bet.sellIn < 6) {
                            updatedOdds++
                        }
                    }
                    updatedOdds
                } else {
                    bet.odds
                }
            }

            else -> bet.odds
        }
        val updatedSellIn = if (bet.type != "First goal scorer") bet.sellIn - 1 else bet.sellIn
        val updatedBet = if (updatedSellIn < 0) {
            if (bet.type != "Total score" && bet.type != "Number of fouls" && bet.odds > 0 && bet.type != "First goal scorer") {
                Bet(bet.type, updatedSellIn, updatedOdds - 1, bet.image)
            } else if (bet.type == "Number of fouls") {
                Bet(bet.type, updatedSellIn, 0, bet.image)
            } else if (bet.type == "Total score") {
                Bet(
                    bet.type,
                    updatedSellIn,
                    if (bet.odds < 50) bet.odds + 1 else bet.odds,
                    bet.image
                )
            } else {
                Bet(bet.type, updatedSellIn, bet.odds, bet.image)
            }
        } else {
            Bet(bet.type, updatedSellIn, updatedOdds, bet.image)
        }
        transformedBets.add(updatedBet)
    }
    return transformedBets
}