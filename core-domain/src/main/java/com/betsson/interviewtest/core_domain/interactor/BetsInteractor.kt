package com.betsson.interviewtest.core_domain.interactor

import com.betson.interviewTest.core_model.common.Bet
import com.betsson.interviewtest.core_data.repository.BetsRepository
import com.betsson.interviewtest.core_data.repository.BetsResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject


interface BetsInteractor {
    fun updateOdds(bets: List<Bet>?): List<Bet>?
    fun getBets(): Flow<BetsPartialState>
}

class BetsInteractorImpl @Inject constructor(private val betsRepository: BetsRepository) :
    BetsInteractor {
    override fun updateOdds(bets: List<Bet>?): List<Bet> {
        return calculateOdds(bets).sortedBy { it.sellIn }
    }

    override fun getBets(): Flow<BetsPartialState> = flow {
        betsRepository.getBets().collect {
            when (it) {
                is BetsResponse.Failed -> {
                    emit(BetsPartialState.Failed(it.errorMsg))
                }

                is BetsResponse.Success -> {
                    emit(BetsPartialState.Success(it.bets?.sortedBy { it.sellIn }))
                }
            }
        }
    }
}

private fun calculateOdds(bets: List<Bet>?): List<Bet> {
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

sealed class BetsPartialState {
    data class Success(val bets: List<Bet>?) : BetsPartialState()
    data class Failed(val errorMessage: String) : BetsPartialState()
}