package com.example.okhttp.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.okhttp.domain.model.Movie
import com.example.okhttp.domain.usecases.SaveMovieUseCase
import com.example.okhttp.domain.usecases.SearchMovieUseCase
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
class SearchMovieListViewModel @Inject constructor(
    private val saveMovieUseCase: SaveMovieUseCase,
    private val searchMovieUseCase: SearchMovieUseCase
) : ViewModel() {

    private var _state = MutableStateFlow<State>(State.ShowLoading)
    val state: StateFlow<State> = _state

    private val _effect: Channel<Effect> = Channel()
    val effect = _effect.receiveAsFlow()

    private fun setState(newState: State) {
        _state.value = newState
    }

    private fun setEffect(effectValue: Effect) {
        viewModelScope.launch { _effect.send(effectValue) }
    }

    fun loadMovies(query: String) = viewModelScope.launch {
        searchMovieUseCase.searchMovie(query).collect { response ->
            setState(State.HideLoading)
            response.result?.let {
                setState(State.FoundMovieList(it))
            }
            response.error?.let {
                setEffect(Effect.ShowToast(it))
                setState(State.Error(it))
            }
        }
    }

    fun search(query: String) = viewModelScope.launch {
        setState(State.ShowLoading)
        searchMovieUseCase.searchMovie(query).collect { response ->
            setState(State.HideLoading)
            response.result?.let {
                setState(State.FoundMovieList(it))
            }
            response.error?.let {
                setEffect(Effect.ShowToast(it))
                setState(State.Error(it))
            }
        }
    }

    fun saveMovie(movieId: Int) = viewModelScope.launch {
        setEffect(Effect.ShowWaitDialog)
        val response = withContext(Dispatchers.IO) {
            saveMovieUseCase.saveMovie(movieId)
        }
        response.result?.let { setEffect(Effect.MovieSaved(movieId)) }
        response.error?.let {
            setState(State.Error(it))
            setEffect(Effect.ShowToast(it))
        }
        setEffect(Effect.HideWaitDialog)
    }

    sealed class State {
        object ShowLoading : State()
        object HideLoading : State()
        data class FoundMovieList(val movies: List<Movie>) : State()
        data class Error(val error: String) : State()
    }

    sealed interface Effect {
        object ShowWaitDialog : Effect
        data class MovieSaved(val movieId: Int) : Effect
        data class ShowToast(var text: String) : Effect
        object HideWaitDialog : Effect
    }
}
