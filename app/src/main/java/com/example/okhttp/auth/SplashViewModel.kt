package com.example.okhttp.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.okhttp.domain.usecases.GetUserPrefsUseCase
import com.example.okhttp.domain.usecases.LoginUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val getUserPrefsUseCase: GetUserPrefsUseCase,
    private val loginUseCase: LoginUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow<State>(State.Empty)
    val state: StateFlow<State> get() = _state

    private val _effect: Channel<Effect> = Channel()
    val effect = _effect.receiveAsFlow()

    private fun setState(newState: State) {
        _state.value = newState
    }

    private fun setEffect(effectValue: Effect) {
        viewModelScope.launch { _effect.send(effectValue) }
    }
    fun checkAccessToken() = viewModelScope.launch {
        if (getUserPrefsUseCase.isAccessTokenEmpty()) {
            getNewToken()
        } else {
            delay(1500L)
            if (getUserPrefsUseCase.isAccessSessionEmpty()) {
                setState(State.GoToAuth)
            } else {
                setState(State.GoToMain)
            }
        }
    }

    private fun getNewToken() = viewModelScope.launch {
        val response = loginUseCase.getNewToken()
        response.error?.let {
            setState(State.Error(it))
            setEffect(Effect.ShowToast(it))
        }
        response.result?.let {
            setState(State.GoToAuth)
        }
    }


    sealed class State {
        object Empty : State()
        object GoToAuth : State()
        object GoToMain : State()
        data class Error(val error: String) : State()
    }

    sealed interface Effect {
        data class ShowToast(var text: String) : Effect
    }
}