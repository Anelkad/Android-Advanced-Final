package com.example.okhttp.movieList

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.okhttp.domain.model.ListItem
import com.example.okhttp.domain.usecases.GetMovieUseCase
import com.example.okhttp.domain.usecases.GetUserPrefsUseCase
import com.example.okhttp.domain.usecases.SaveMovieUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class MovieListViewModel @Inject constructor(
    private val getMovieUseCase: GetMovieUseCase,
    private val saveMovieUseCase: SaveMovieUseCase,
    private val getUserPrefsUseCase: GetUserPrefsUseCase
) : ViewModel() {

    private var _state = MutableStateFlow<State>(State.ShowLoading)
    val state: StateFlow<State> = _state

    private val _effect: Channel<Effect> = Channel()
    val effect = _effect.receiveAsFlow()

    val pagedMovieList: Flow<PagingData<ListItem>> =
        getMovieUseCase.getPagedMovieList().cachedIn(viewModelScope)

    private fun setState(newState: State) {
        _state.value = newState
    }

    private fun setEffect(effectValue: Effect) {
        viewModelScope.launch { _effect.send(effectValue) }
    }

    fun saveMovie(movieId: Int) = viewModelScope.launch {
        if (getUserPrefsUseCase.isAccessSessionEmpty()) {
            setEffect(Effect.NoAccess)
            return@launch
        }
        setEffect(Effect.ShowWaitDialog)
        val response = withContext(Dispatchers.IO) {
            saveMovieUseCase.saveMovie(movieId)
        }
        response.result?.let { setEffect(Effect.MovieSaved(movieId))  }
        response.error?.let {
            setState(State.Error(it))
            setEffect(Effect.ShowToast(it))
        }
        setEffect(Effect.HideWaitDialog)
    }

    sealed class State {
        object ShowLoading : State()
        object HideLoading : State()
        data class Error(val error: String) : State()
    }

    sealed interface Effect {
        object NoAccess : Effect
        object ShowWaitDialog : Effect
        data class MovieSaved(val movieId: Int) : Effect
        data class ShowToast(var text: String) : Effect
        object HideWaitDialog : Effect
    }
}