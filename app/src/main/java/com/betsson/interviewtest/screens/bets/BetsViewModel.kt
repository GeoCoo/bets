package com.betsson.interviewtest.screens.bets

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.android.sdsc.base.MviViewModel
import com.android.sdsc.base.ViewEvent
import com.android.sdsc.base.ViewSideEffect
import com.android.sdsc.base.ViewState
import com.betsson.interviewtest.model.Bet
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


data class State(
    var isLoading: Boolean = false, val bets: List<Bet>? = listOf(),
    val errorMessage: String? = ""
) : ViewState

sealed class Event : ViewEvent {
    data object GetInitBets : Event()
    data class UpdateOdds(val bets: List<Bet>?) : Event()
    data class NavigateToSingleBet(val navController: NavController,val type: String) : Event()
}

sealed class Effect : ViewSideEffect {}

@HiltViewModel
class MainViewModel @Inject constructor(
    private val mainInteractor: MainInteractor
) : MviViewModel<Event, State, Effect>() {
    override fun setInitialState(): State = State(
        isLoading = true
    )

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun handleEvents(event: Event) {
        when (event) {
            is Event.GetInitBets -> {
                viewModelScope.launch {
                    mainInteractor.getBets().collect {
                        when (it) {
                            is BetsPartialState.Failed -> {
                                setState {
                                    copy(
                                        isLoading = false,
                                        bets = null,
                                        errorMessage = it.errorMessage
                                    )
                                }
                            }

                            is BetsPartialState.Success -> {
                                setState {
                                    copy(
                                        isLoading = false,
                                        bets = it.bets
                                    )
                                }
                            }
                        }

                    }
                }
            }

            is Event.UpdateOdds -> {
                setState {
                    copy(
                        isLoading = false,
                        bets = mainInteractor.updateOdds(event.bets)
                    )
                }
            }

            is Event.NavigateToSingleBet -> {
                mainInteractor.navigateToSingle(navController = event.navController,event.type)
            }
        }
    }
}

