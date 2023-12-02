package com.example.okhttp.savedMovieList

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.okhttp.domain.usecases.SavedMovieUseCase
import com.example.okhttp.domain.model.Movie
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class SavedMovieListViewModel @Inject constructor (
    private val savedMovieUseCase: SavedMovieUseCase
) : ViewModel() {

    private var _state = MutableStateFlow<State>(State.ShowLoading)
    val state: StateFlow<State> = _state

    private var movies: List<Movie> = emptyList()

    init {
        getMovieList()
    }

    fun getMovieList() = viewModelScope.launch {
        if (movies.isEmpty()) {
            val response = savedMovieUseCase.getSavedMovieList()
            response.result?.let {
                _state.value = State.SavedMovieList(it)
                movies = it
            }
            response.error?.let {
                _state.value = State.Error(it)
            }
        } else {
            _state.value = State.SavedMovieList(movies)
            delay(1L)
        }
        _state.value = State.HideLoading
    }


    fun deleteMovie(movieId: Int) = viewModelScope.launch {
        _state.value = State.ShowWaitDialog
        val isMovieDeleted = withContext(Dispatchers.IO) {
            savedMovieUseCase.deleteMovie(movieId)
        }
        _state.value = State.MovieDeleted(isMovieDeleted)
        _state.value = State.HideWaitDialog
    }

    sealed class State {
        object ShowLoading : State()
        object HideLoading : State()
        data class SavedMovieList(val movies: List<Movie>) : State()
        data class MovieDeleted(val movieId: Int) : State()
        object HideWaitDialog : State()
        object ShowWaitDialog : State()
        data class Error(val error: String) : State()
    }
}
