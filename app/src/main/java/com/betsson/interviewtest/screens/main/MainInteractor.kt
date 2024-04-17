package com.betsson.interviewtest.screens.main

import com.betsson.interviewtest.model.Bet
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject


interface MainInteractor {
    fun updateOdds()
    fun navigateToSingle(type: String): Bet
    fun getBets(): Flow<BetsPartialState>

}

class MainInteractorImpl @Inject constructor(private val betsRepository: BetsRepository) :
    MainInteractor {
    override fun updateOdds() {
    }

    override fun navigateToSingle(type: String): Bet {return Bet("",0,0,"")}

    override fun getBets(): Flow<BetsPartialState> = flow {
        betsRepository.getBets().collect {
            when (it) {
                is BetsResponse.Failed -> {
                    emit(BetsPartialState.Failed(it.errorMsg))
                }

                is BetsResponse.Success -> {
                    emit(BetsPartialState.Success(it.bets))
                }
            }
        }
    }

}

sealed class BetsPartialState {
    data class Success(val bets: List<Bet>?) : BetsPartialState()
    data class Failed(val errorMessage: String) : BetsPartialState()
}