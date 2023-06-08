package com.example.okhttp.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.okhttp.models.MovieDetails
import com.example.okhttp.repository.MovieRepository
import com.example.okhttp.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieDetailsViewModel @Inject constructor(
    private val repository: MovieRepository
): ViewModel() {

    private val _movieDetailsDetailsState = MutableLiveData<Resource<MovieDetails>>(null)
    val movieDetailsDetailsState: LiveData<Resource<MovieDetails>> = _movieDetailsDetailsState

    fun getMovie(movieId: Int) = viewModelScope.launch(Dispatchers.IO) {
        _movieDetailsDetailsState.postValue(Resource.Loading)
        val result = repository.getMovie(movieId)
        _movieDetailsDetailsState.postValue(result)
    }

}