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

    private val _state = MutableStateFlow<State>(State.Empty)
    val state: StateFlow<State> get() = _state

    private fun setState(newState: State) {
        _state.value = newState
    }
    fun logout() {
        logoutUseCase.logout()
        setState(State.LoggedOut)
    }

    fun getProfile() {
        if (getUserPrefsUseCase.isAccessSessionEmpty()){
            setState(State.NoAccess)
            return
        }
        setState(State.Profile(
            id = getUserPrefsUseCase.getUid(),
            username = getUserPrefsUseCase.getUsername(),
            session = getUserPrefsUseCase.getSession()
        ))
    }


    sealed class State {
        object Empty : State()
        object NoAccess : State()
        data class Profile( val id: String, val username: String, val session: String) : State()
        object LoggedOut : State()
    }
}