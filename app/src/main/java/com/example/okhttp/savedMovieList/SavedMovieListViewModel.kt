package com.example.okhttp.savedMovieList

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.okhttp.domain.model.Movie
import com.example.okhttp.domain.usecases.GetSavedMovieUseCase
import com.example.okhttp.domain.usecases.SaveMovieUseCase
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
class SavedMovieListViewModel @Inject constructor(
    private val saveMovieUseCase: SaveMovieUseCase,
    private val getSavedMovieUseCase: GetSavedMovieUseCase
) : ViewModel() {

    private var _state = MutableStateFlow<State?>(null)
    val state: StateFlow<State?> = _state

    private val _effect: Channel<Effect> = Channel()
    val effect = _effect.receiveAsFlow()

    init {
        getMovieList()
    }

    private fun setState(newState: State) {
        _state.value = newState
    }

    private fun setEffect(effectValue: Effect) {
        viewModelScope.launch { _effect.send(effectValue) }
    }

    fun getMovieList() = viewModelScope.launch {
        setState(State.ShowLoading)
        getSavedMovieUseCase.getSavedMovieList().collect { response ->
            setState(State.HideLoading)
            response.result?.let {
                setState(State.SavedMovieList(it))
            }
            response.error?.let {
                setEffect(Effect.ShowToast(it))
                setState(State.Error(it))
            }
        }
    }

    fun deleteMovie(movieId: Int) = viewModelScope.launch {
        setEffect(Effect.ShowWaitDialog)
        val response = withContext(Dispatchers.IO) {
            saveMovieUseCase.deleteMovie(movieId)
        }
        response.result?.let {
            setEffect(
                Effect.MovieDeleted(
                    success = it.success,
                    movieId = movieId
                )
            )
            val newList = (state.value as State.SavedMovieList).movies.toMutableList()
            val deletedMovie = newList.find { it.id == movieId }
            newList.remove(deletedMovie)
            setState(State.SavedMovieList(newList))
        }
        response.error?.let {
            setEffect(Effect.ShowToast(it))
            setState(State.Error(it))
        }
        setEffect(Effect.HideWaitDialog)
    }

    sealed class State {
        object ShowLoading : State()
        object HideLoading : State()
        data class SavedMovieList(val movies: List<Movie>) : State()
        data class Error(val error: String) : State()
    }

    sealed interface Effect {
        object ShowWaitDialog : Effect
        data class MovieDeleted(val success: Boolean, val movieId: Int) : Effect
        data class ShowToast(var text: String) : Effect
        object HideWaitDialog : Effect
    }
}
