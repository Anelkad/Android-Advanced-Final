package com.example.okhttp.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.okhttp.domain.usecases.GetUserPrefsUseCase
import com.example.okhttp.domain.usecases.LoginUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val getUserPrefsUseCase: GetUserPrefsUseCase,
    private val loginUseCase: LoginUseCase,
) : ViewModel(){

    private val _state = MutableStateFlow<State>(State.ShowLoading)
    val state: StateFlow<State> get() = _state

    fun getNewToken() {
        viewModelScope.launch {
            val response = loginUseCase.getNewToken()
            response.error?.let {
                _state.value = State.Error(it)
            }
            response.result?.let {
                _state.value = State.NewToken(it)
            }
            _state.value = State.HideLoading
        }
    }

    fun login(username: String, password: String) {
        viewModelScope.launch {
            val response = loginUseCase.login(username, password)
            response?.error?.let {
                _state.value = State.Error(it)
            }
            response?.result?.let {
                _state.value = State.LoggedIn(it && !getUserPrefsUseCase.isAccessTokenEmpty())
            }
            _state.value = State.HideLoading
        }
    }


    sealed class State {
        object ShowLoading : State()
        object HideLoading : State()
        data class NewToken(val token: String) : State()
        data class Error(val error: String) : State()
        data class LoggedIn(val loggedIn: Boolean) : State()
    }
}