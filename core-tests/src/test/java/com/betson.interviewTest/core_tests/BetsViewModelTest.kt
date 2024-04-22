package com.betson.interviewTest.core_tests

import androidx.lifecycle.ViewModel
import com.betson.interviewTest.core_tests.helpers.CoroutineTestRule
import com.betson.interviewTest.core_tests.helpers.RobolectricTest
import com.betson.interviewTest.core_tests.helpers.getItemsFromNetwork
import com.betson.interviewTest.core_tests.helpers.runFlowTest
import com.betson.interviewTest.core_tests.helpers.runTest
import com.betson.interviewTest.core_tests.helpers.toFlow
import com.betson.interviewTest.feature_bets_screen.ui.BetsViewModel
import com.betson.interviewTest.feature_bets_screen.ui.Event
import com.betson.interviewTest.feature_bets_screen.ui.State
import com.betsson.interviewtest.core_domain.interactor.BetsInteractor
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

class BetsViewModelTest : RobolectricTest() {
    @get:Rule
    val coroutineRule = CoroutineTestRule()

    @Spy
    private lateinit var betsInteractor: BetsInteractor

    private lateinit var betsViewModel: BetsViewModel

    private var initialState = State(isLoading = true)

    @Before
    fun before() {
        MockitoAnnotations.openMocks(this)
        betsViewModel = BetsViewModel(betsInteractor)
    }

    @After
    fun after() {
        coroutineRule.cancelScopeAndDispatcher()
    }

    @Test
    fun `When Event#GetInitBets and BetsPartialState#Susccess then assert state`() {
        coroutineRule.runTest {
            getBetsInterceptor(BetsPartialState.Success(getItemsFromNetwork()))
            val event = Event.GetInitBets
            betsViewModel.setEvent(event)

            betsViewModel.viewStateHistory.runFlowTest {
                val state = awaitItem()
                TestCase.assertEquals(
                    state, initialState.copy(
                        isLoading = false,
                        bets = getItemsFromNetwork()
                    )
                )
            }
        }
    }

    @Test
    fun `When Event#GetInitBets and BetsPartialState#Failed then assert state`() {
        coroutineRule.runTest {
            getBetsInterceptor(BetsPartialState.Failed(""))
            val event = Event.GetInitBets
            betsViewModel.setEvent(event)

            betsViewModel.viewStateHistory.runFlowTest {
                val state = awaitItem()
                TestCase.assertEquals(
                    state, initialState.copy(
                        isLoading = false,
                        bets = null,
                        errorMessage = ""
                    )
                )
            }
        }
    }


    private fun getBetsInterceptor(partialState: BetsPartialState) {
        Mockito.`when`(betsInteractor.getBets()).thenReturn(partialState.toFlow())
    }
}