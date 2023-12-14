package com.example.okhttp.movieDetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.okhttp.domain.model.MovieDetails
import com.example.okhttp.domain.model.MovieIsSaved
import com.example.okhttp.domain.usecases.GetMovieUseCase
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
class MovieDetailsViewModel @Inject constructor(
    private val getMovieUseCase: GetMovieUseCase,
    private val saveMovieUseCase: SaveMovieUseCase
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

    fun getMovie(movieId: Int) = viewModelScope.launch {
        val response = withContext(Dispatchers.IO) {
            getMovieUseCase.getMovie(movieId)
        }
        setState(State.HideLoading)
        response.result?.let {
            setState(State.ShowMovieDetails(movie = it))
        }
        response.error?.let {
            setState(State.Error(it))
            setEffect(Effect.ShowToast(it))
        }
    }

    fun getIsMovieSaved(movieId: Int) = viewModelScope.launch {
        val response = withContext(Dispatchers.IO) {
            getMovieUseCase.getIsMovieSaved(movieId)
        }
        setState(State.HideLoading)
        response.result?.let {
            setState(State.IsMovieSaved(details = it))
        }
        response.error?.let {
            setState(State.Error(it))
            setEffect(Effect.ShowToast(it))
        }
    }

    fun saveMovie(movieId: Int) = viewModelScope.launch {
        setEffect(Effect.ShowWaitDialog)
        val response = withContext(Dispatchers.IO) {
            saveMovieUseCase.saveMovie(movieId)
        }
        response.result?.let { setEffect(Effect.MovieSaved) }
        response.error?.let {
            setState(State.Error(it))
            setEffect(Effect.ShowToast(it))
        }
       setEffect(Effect.HideWaitDialog)
    }

    fun deleteMovie(movieId: Int) = viewModelScope.launch {
        setEffect(Effect.ShowWaitDialog)
        val response = withContext(Dispatchers.IO) {
            saveMovieUseCase.deleteMovie(movieId)
        }
        response.result?.let { setEffect(Effect.MovieDeleted) }
        response.error?.let {
            setState(State.Error(it))
            setEffect(Effect.ShowToast(it))
        }
        setEffect(Effect.HideWaitDialog)
    }

    sealed class State {
        object ShowLoading : State()
        object HideLoading : State()
        data class ShowMovieDetails(val movie: MovieDetails) : State()
        data class IsMovieSaved(val details: MovieIsSaved) : State()
        data class Error(val error: String) : State()
    }

    sealed interface Effect {
        object ShowWaitDialog : Effect
        object MovieSaved : Effect
        object MovieDeleted : Effect
        data class ShowToast(var text: String) : Effect
        object HideWaitDialog : Effect
    }

}