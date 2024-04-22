package com.betson.interviewTest.core_tests

import com.betson.interviewTest.core_model.common.Bet
import com.betson.interviewTest.core_tests.helpers.CoroutineTestRule
import com.betson.interviewTest.core_tests.helpers.RobolectricTest
import com.betson.interviewTest.core_tests.helpers.getItemsFromNetwork
import com.betson.interviewTest.core_tests.helpers.runFlowTest
import com.betson.interviewTest.core_tests.helpers.runTest
import com.betson.interviewtest.core_api.api.ApiClient
import com.betson.interviewtest.core_api.api.ApiClientImpl
import com.betson.interviewtest.core_api.api.ApiService
import com.betsson.interviewtest.core_data.repository.BetsRepository
import com.betsson.interviewtest.core_data.repository.BetsRepositoryImpl
import com.betsson.interviewtest.core_data.repository.BetsResponse
import junit.framework.TestCase
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.ArgumentMatcher
import org.mockito.ArgumentMatchers
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.mockito.Spy
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.stub
import org.mockito.kotlin.times
import retrofit2.Response


class TestsBetsRepository : RobolectricTest() {

    @get:Rule
    val coroutineRule = CoroutineTestRule()

    @Spy
    private lateinit var betsRepository: BetsRepository

    @Spy
    lateinit var apiClient: ApiClient


    @Before
    fun before() {
        MockitoAnnotations.openMocks(this)
        betsRepository = BetsRepositoryImpl(apiClient)

    }

    @After
    fun after() {
        coroutineRule.cancelScopeAndDispatcher()
    }

    @Test
    fun `Given retrieveBets success, When retrieveBets is called, Then PartialState BetsResponse Success Emitted `() {
        coroutineRule.runTest {
            val response: Response<List<Bet>> = Response.success(getItemsFromNetwork())
            requestBetsInterceptor(response)

            betsRepository.getBets().runFlowTest {
                Mockito.verify(apiClient, times(1)).retrieveBets()
                TestCase.assertEquals(BetsResponse.Success(getItemsFromNetwork()), awaitItem())
            }
        }
    }

    @Test
    fun `Given retrieveBets error, When retrieveBets is called, Then PartialState BetsResponse error Emitted `() {
        coroutineRule.runTest {
            val response: Response<List<Bet>> =
                Response.error(400, "error".toResponseBody())
            requestBetsInterceptor(response)

            betsRepository.getBets().runFlowTest {
                Mockito.verify(apiClient, times(1)).retrieveBets()
                TestCase.assertEquals(
                    BetsResponse.Failed("error"),
                    awaitItem()
                )
            }
        }
    }


    private fun requestBetsInterceptor(response: Response<List<Bet>>) {
        apiClient.stub {
            onBlocking {
                retrieveBets()
            }.doReturn(
                response
            )
        }
    }

}