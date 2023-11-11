package com.example.okhttp.movieDetails

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.okhttp.models.Movie
import com.example.okhttp.models.MovieDetails
import com.example.okhttp.movieList.MovieUseCase
import com.example.okhttp.savedMovieList.SavedMovieUseCase
import com.example.okhttp.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieDetailsViewModel @Inject constructor(
    private val movieUseCase: MovieUseCase,
    private val savedMovieUseCase: SavedMovieUseCase
): ViewModel() {

    private val _movieDetailsDetailsState = MutableLiveData<Resource<MovieDetails>>(null)
    val movieDetailsDetailsState: LiveData<Resource<MovieDetails>> = _movieDetailsDetailsState

    private val _saveMovieState = MutableLiveData<Resource<Movie>>(null)
    val saveMovieState: LiveData<Resource<Movie>> = _saveMovieState

    fun getMovie(movieId: Int) = viewModelScope.launch(Dispatchers.IO) {
        _movieDetailsDetailsState.postValue(Resource.Loading)
        val result = movieUseCase.getMovie(movieId)
        _movieDetailsDetailsState.postValue(Resource.Success(result))
    }

    fun saveMovie(movie: Movie) = viewModelScope.launch {
        _saveMovieState.value = Resource.Loading
        val result = savedMovieUseCase.saveMovie(movie)
        _saveMovieState.value = result
    }

}