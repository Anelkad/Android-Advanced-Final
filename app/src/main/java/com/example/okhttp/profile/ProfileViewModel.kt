package com.example.okhttp.profile

import androidx.lifecycle.ViewModel
import com.example.okhttp.domain.usecases.GetUserPrefsUseCase
import com.example.okhttp.domain.usecases.LogoutUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val getUserPrefsUseCase: GetUserPrefsUseCase,
    private val logoutUseCase: LogoutUseCase
) : ViewModel(){

    private val _state = MutableStateFlow<State>(State.ShowLoading)
    val state: StateFlow<State> get() = _state

    fun logout() {
        logoutUseCase.logout()
        _state.value = State.LoggedOut
    }


    sealed class State {
        object ShowLoading : State()
        object HideLoading : State()
        data class NewToken(val token: String) : State()
        data class Error(val error: String) : State()
        data class LoggedIn(val loggedIn: Boolean) : State()
        object LoggedOut : State()
    }
}