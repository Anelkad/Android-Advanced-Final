package com.example.okhttp.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.okhttp.models.Movie
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

    private val _movieDetailsState = MutableLiveData<Resource<Movie>>(null)
    val movieDetailsState: LiveData<Resource<Movie>> = _movieDetailsState

    fun getMovie(movieId: Int) = viewModelScope.launch(Dispatchers.IO) {
        _movieDetailsState.postValue(Resource.Loading)
        val result = repository.getMovie(movieId)
        _movieDetailsState.postValue(result)
    }
}