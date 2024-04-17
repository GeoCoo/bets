package com.betsson.interviewtest.screens.bets

import com.betsson.interviewtest.model.Bet
import com.betsson.interviewtest.networking.ApiClient
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

interface BetsRepository {
    fun getBets(): Flow<BetsResponse>
}

class BetsRepositoryImpl @Inject constructor(private val apiClient: ApiClient) : BetsRepository {
    override fun getBets(): Flow<BetsResponse> = flow {
        //For the scope of the exercise because there is no actual call
        // i return the function getItemsFromNetwork(
        // instead of an actual response.body
        val response = apiClient.retrieveBets()
        if (response.isSuccessful) {
            emit(BetsResponse.Success(response.body()))
        } else {
            emit(BetsResponse.Failed(response.message()))
        }
    }
}


sealed class BetsResponse {
    data class Success(val bets: List<Bet>?) : BetsResponse()
    data class Failed(val errorMsg: String) : BetsResponse()
}