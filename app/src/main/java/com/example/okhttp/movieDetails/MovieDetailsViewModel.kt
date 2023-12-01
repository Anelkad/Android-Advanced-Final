package com.example.okhttp.movieDetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.usecases.MovieUseCase
import com.example.domain.usecases.SavedMovieUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class MovieDetailsViewModel @Inject constructor(
    private val movieUseCase: MovieUseCase,
    private val savedMovieUseCase: SavedMovieUseCase
): ViewModel() {

    private var _state = MutableStateFlow<State>(State.ShowLoading)
    val state: StateFlow<State> = _state

    fun getMovie(movieId: Int) = viewModelScope.launch {
        val response = withContext(Dispatchers.IO) {
            val result = movieUseCase.getMovie(movieId)
            result
        }
        response.result?.let {
            _state.value = State.ShowMovieDetails(movie = it)
            _state.value = State.HideLoading
        }
        response.error?.let {
            _state.value = State.Error(it)
            _state.value = State.HideLoading
        }
    }

    fun saveMovie(movie: com.example.domain.model.Movie) = viewModelScope.launch {
        _state.value = State.ShowWaitDialog
        val isMovieSaved = withContext(Dispatchers.IO){
            savedMovieUseCase.saveMovie(movie)
        }
        _state.value = State.MovieSaved(isMovieSaved)
        _state.value = State.HideWaitDialog
    }

    sealed class State {
        object ShowLoading : State()
        object HideLoading : State()
        data class MovieSaved(val movie: com.example.domain.model.Movie) : State()
        data class ShowMovieDetails(val movie: com.example.domain.model.MovieDetails) : State()
        object HideWaitDialog : State()
        object ShowWaitDialog : State()
        data class Error(val error: String) : State()
    }

}