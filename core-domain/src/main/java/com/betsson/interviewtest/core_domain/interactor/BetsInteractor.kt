package com.betsson.interviewtest.core_domain.interactor

import com.betson.interviewTest.core_model.common.Model
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject


interface MainInteractor {
    fun updateOdds(bets: List<Model>?): List<Model>?
    fun getBets(): Flow<BetsPartialState>
}

class MainInteractorImpl @Inject constructor(private val betsRepository: com.betsson.interviewtest.core_data.repository.BetsRepository) :
    MainInteractor {
    override fun updateOdds(bets: List<Model>?): List<Model> {
        return calculateOdds(bets).sortedBy { it.sellIn }
    }

    override fun getBets(): Flow<BetsPartialState> = flow {
        betsRepository.getBets().collect {
            when (it) {
                is com.betsson.interviewtest.core_data.repository.BetsResponse.Failed -> {
                    emit(BetsPartialState.Failed(it.errorMsg))
                }

                is com.betsson.interviewtest.core_data.repository.BetsResponse.Success -> {
                    emit(BetsPartialState.Success(it.bets?.sortedBy { it.sellIn }))
                }
            }
        }
    }
}

private fun calculateOdds(bets: List<Model>?): List<Model> {
    val transformedBets = mutableListOf<Model>()
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
                Model(bet.type, updatedSellIn, updatedOdds - 1, bet.image)
            } else if (bet.type == "Number of fouls") {
                Model(bet.type, updatedSellIn, 0, bet.image)
            } else if (bet.type == "Total score") {
                Model(
                    bet.type,
                    updatedSellIn,
                    if (bet.odds < 50) bet.odds + 1 else bet.odds,
                    bet.image
                )
            } else {
                Model(bet.type, updatedSellIn, bet.odds, bet.image)
            }
        } else {
            Model(bet.type, updatedSellIn, updatedOdds, bet.image)
        }
        transformedBets.add(updatedBet)
    }
    return transformedBets
}

sealed class BetsPartialState {
    data class Success(val bets: List<Model>?) : BetsPartialState()
    data class Failed(val errorMessage: String) : BetsPartialState()
}