package com.betsson.interviewtest.networking



import com.betsson.interviewtest.model.Bet
import retrofit2.Response
import retrofit2.http.GET
import javax.inject.Inject


interface ApiService {

    @GET("bets")
    suspend fun retrieveBets():Response<List<Bet>>
}


interface ApiClient {
    suspend fun retrieveBets():Response<List<Bet>>

}

class ApiClientImpl @Inject constructor(private val apiService: ApiService) : ApiClient {
    override suspend fun retrieveBets(): Response<List<Bet>> = apiService.retrieveBets()


}

