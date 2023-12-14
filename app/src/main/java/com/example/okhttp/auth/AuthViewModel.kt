package com.example.okhttp.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.okhttp.domain.usecases.LoginUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
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

    fun login(username: String, password: String) = viewModelScope.launch {
        if (username.isEmpty() || password.isEmpty()) {
            setEffect(Effect.ShowToast("Complete all fields!"))
            return@launch
        }
        setEffect(Effect.ShowWaitDialog)
        val response = withContext(Dispatchers.IO) {
            loginUseCase.login(username, password)
        }
        response?.error?.let {
            setState(State.Error(it))
            setEffect(Effect.ShowToast(it))
        }
        response?.result?.let {
            setState(State.GoToMain)
        }
        setEffect(Effect.HideWaitDialog)
    }


    sealed class State {
        object Empty : State()
        object GoToMain : State()
        data class Error(val error: String) : State()
    }

    sealed interface Effect {
        data class ShowToast(var text: String) : Effect
        object ShowWaitDialog : Effect
        object HideWaitDialog : Effect
    }
}