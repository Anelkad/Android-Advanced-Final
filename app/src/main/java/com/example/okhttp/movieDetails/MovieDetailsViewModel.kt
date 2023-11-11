package com.example.okhttp.movieDetails

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.okhttp.models.MovieDetails
import com.example.okhttp.movieList.MovieUseCase
import com.example.okhttp.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieDetailsViewModel @Inject constructor(
    private val movieUseCase: MovieUseCase
): ViewModel() {

    private val _movieDetailsDetailsState = MutableLiveData<Resource<MovieDetails>>(null)
    val movieDetailsDetailsState: LiveData<Resource<MovieDetails>> = _movieDetailsDetailsState

    fun getMovie(movieId: Int) = viewModelScope.launch(Dispatchers.IO) {
        _movieDetailsDetailsState.postValue(Resource.Loading)
        val result = movieUseCase.getMovie(movieId)
        _movieDetailsDetailsState.postValue(Resource.Success(result))
    }

}