package com.betson.interviewTest.feature_bets_screen.ui

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.viewModelScope
import com.android.sdsc.base.MviViewModel
import com.android.sdsc.base.ViewEvent
import com.android.sdsc.base.ViewSideEffect
import com.android.sdsc.base.ViewState
import com.betson.interviewTest.core_model.common.Model
import com.betson.interviewTest.core_resources.R.*
import com.betson.interviewTest.core_resources.provider.ResourceProvider
import com.betsson.interviewtest.core_domain.interactor.MainInteractor
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


data class State(
    var isLoading: Boolean = false, val bets: List<Model>? = listOf(),
    val errorMessage: String? = "",
    val appName: String = ""
) : ViewState

sealed class Event : ViewEvent {
    data object GetInitBets : Event()
    data class UpdateOdds(val bets: List<Model>?) : Event()
}

sealed class Effect : ViewSideEffect {}

@HiltViewModel
class MainViewModel @Inject constructor(
    private val mainInteractor: MainInteractor,
    private val resourceProvider: ResourceProvider
) : MviViewModel<Event, State, Effect>() {
    override fun setInitialState(): State = State(
        isLoading = true,
        appName = resourceProvider.getString(string.app_name)
    )

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun handleEvents(event: Event) {
        when (event) {
            is Event.GetInitBets -> {
                viewModelScope.launch {
                    mainInteractor.getBets().collect {
                        when (it) {
                            is com.betsson.interviewtest.core_domain.interactor.BetsPartialState.Failed -> {
                                setState {
                                    copy(
                                        isLoading = false,
                                        bets = null,
                                        errorMessage = it.errorMessage
                                    )
                                }
                            }

                            is com.betsson.interviewtest.core_domain.interactor.BetsPartialState.Success -> {
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
        }
    }
}

