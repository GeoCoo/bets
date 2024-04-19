package com.betsson.interviewtest.core_data.repository

import com.betson.interviewtest.core_api.api.ApiClient
import com.betson.interviewTest.core_model.common.Model
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

interface BetsRepository {
    fun getBets(): Flow<BetsResponse>
}

class BetsRepositoryImpl @Inject constructor(private val apiClient: ApiClient) :
    BetsRepository {
    override fun getBets(): Flow<BetsResponse> = flow {
        //For the scope of the exercise because there is no actual call
        //I return the function data from the give function getItemsFromNetwork()
        //transformed to json through and interceptor
        val response = apiClient.retrieveBets()
        if (response.isSuccessful) {
            emit(BetsResponse.Success(response.body()))
        } else {
            emit(BetsResponse.Failed(response.message()))
        }
    }
}

sealed class BetsResponse {
    data class Success(val bets: List<Model>?) : BetsResponse()
    data class Failed(val errorMsg: String) : BetsResponse()
}