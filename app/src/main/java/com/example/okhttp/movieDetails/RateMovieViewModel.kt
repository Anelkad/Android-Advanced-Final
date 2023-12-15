package com.example.okhttp.movieDetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.okhttp.domain.usecases.RateMovieUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RateMovieViewModel @Inject constructor(
    private val rateMovieUseCase: RateMovieUseCase
) : ViewModel() {

    private var _state = MutableStateFlow<State>(State.HideLoading)
    val state: StateFlow<State> = _state

    fun rateMovie(movieId: Int, rating: Double) = viewModelScope.launch {
            _state.value = State.ShowLoading
            val response = rateMovieUseCase.rateMovie(
                movieId = movieId,
                rating = rating
            )
            response.result?.let { _state.value = State.RatedMovie(rating) }
            response.error?.let { _state.value = State.Error(it) }
            _state.value = State.HideLoading
        }

    sealed class State {
        object ShowLoading : State()
        object HideLoading : State()
        data class RatedMovie(val rating: Double) : State()
        data class Error(val error: String) : State()
    }
}