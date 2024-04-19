package com.betson.interviewtest.core_api.api

import com.betson.interviewTest.core_model.common.Model
import retrofit2.Response
import retrofit2.http.GET
import javax.inject.Inject

interface ApiService {

    @GET("bets")
    suspend fun retrieveBets(): Response<List<Model>>
}


interface ApiClient {
    suspend fun retrieveBets():Response<List<Model>>
}

class ApiClientImpl @Inject constructor(private val apiService: ApiService) : ApiClient {
    override suspend fun retrieveBets(): Response<List<Model>> = apiService.retrieveBets()
}

