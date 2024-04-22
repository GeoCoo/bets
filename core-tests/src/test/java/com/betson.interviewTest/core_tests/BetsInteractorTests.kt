package com.betson.interviewTest.core_tests

import com.betson.interviewTest.core_tests.helpers.CoroutineTestRule
import com.betson.interviewTest.core_tests.helpers.RobolectricTest
import com.betson.interviewTest.core_tests.helpers.getItemsFromNetwork
import com.betson.interviewTest.core_tests.helpers.runFlowTest
import com.betson.interviewTest.core_tests.helpers.runTest
import com.betson.interviewTest.core_tests.helpers.toFlow
import com.betsson.interviewtest.core_data.repository.BetsRepository
import com.betsson.interviewtest.core_data.repository.BetsResponse
import com.betsson.interviewtest.core_domain.interactor.BetsInteractor
import com.betsson.interviewtest.core_domain.interactor.BetsInteractorImpl
import com.betsson.interviewtest.core_domain.interactor.BetsPartialState
import junit.framework.TestCase
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.ArgumentMatchers
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.mockito.Spy

class BetsInteractorTests : RobolectricTest() {

    @get:Rule
    val coroutineRule = CoroutineTestRule()
    private lateinit var betesInteractor: BetsInteractor


    @Spy
    private lateinit var betsRepository: BetsRepository


    @Before
    fun before() {
        MockitoAnnotations.openMocks(this)
        betesInteractor = BetsInteractorImpl(betsRepository)
    }

    @After
    fun after() {
        coroutineRule.cancelScopeAndDispatcher()
    }


    @Test
    fun `Given success from BetsResponse, when getBets, then BetsPartialState success emitted`() {
        coroutineRule.runTest {
            retrieveBetsInterceptor(BetsResponse.Success(getItemsFromNetwork()))
            betesInteractor.getBets().runFlowTest {
                TestCase.assertEquals(BetsPartialState.Success(getItemsFromNetwork().sortedBy { it.sellIn }), awaitItem())
            }
        }
    }

    @Test
    fun `Given Failed from BetsResponse, when getBets, then BetsPartialState failed emitted`() {
        coroutineRule.runTest {
            retrieveBetsInterceptor(BetsResponse.Failed(errorMsg = ""))
            betesInteractor.getBets().runFlowTest {
                TestCase.assertEquals(
                    BetsPartialState.Failed(""),
                    awaitItem()
                )
            }
        }
    }


    private fun retrieveBetsInterceptor(betsResponse: BetsResponse) {
        Mockito.`when`(betsRepository.getBets()).thenReturn(betsResponse.toFlow())

    }
}