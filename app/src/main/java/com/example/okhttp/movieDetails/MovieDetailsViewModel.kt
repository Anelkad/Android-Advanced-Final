package com.example.okhttp.movieDetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.okhttp.domain.model.MovieDetails
import com.example.okhttp.domain.usecases.GetMovieUseCase
import com.example.okhttp.domain.usecases.SaveMovieUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class MovieDetailsViewModel @Inject constructor(
    private val getMovieUseCase: GetMovieUseCase,
    private val saveMovieUseCase: SaveMovieUseCase
): ViewModel() {

    private var _state = MutableStateFlow<State>(State.ShowLoading)
    val state: StateFlow<State> = _state

    fun getMovie(movieId: Int) = viewModelScope.launch {
        val response = withContext(Dispatchers.IO) {
            getMovieUseCase.getMovie(movieId)
        }
        response.result?.let {
            _state.value = State.HideLoading
            _state.value = State.ShowMovieDetails(movie = it)
        }
        response.error?.let {
            _state.value = State.HideLoading
            _state.value = State.Error(it)
        }
    }

    fun saveMovie(movieId: Int) = viewModelScope.launch {
        _state.value = State.ShowWaitDialog
        val response = withContext(Dispatchers.IO){
            saveMovieUseCase.saveMovie(movieId)
        }
        response.result?.let { _state.value = State.MovieSaved(it.success) }
        response.error?.let { _state.value = State.Error(it) }
        _state.value = State.HideWaitDialog
    }

    sealed class State {
        object ShowLoading : State()
        object HideLoading : State()
        data class MovieSaved(val success: Boolean) : State()
        data class ShowMovieDetails(val movie: MovieDetails) : State()
        object HideWaitDialog : State()
        object ShowWaitDialog : State()
        data class Error(val error: String) : State()
    }

}