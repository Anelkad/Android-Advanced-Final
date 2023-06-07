package com.example.okhttp.viewmodels

import BASE_URL
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.okhttp.models.MoviePage
import com.example.okhttp.repository.MovieRepository
import com.example.okhttp.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieListViewModel @Inject constructor(
    private val repository: MovieRepository
) : ViewModel() {

    private val _movieListState = MutableLiveData<Resource<MoviePage>>(null)
    val movieListState: LiveData<Resource<MoviePage>> =_movieListState
    init {
        getMovieList(BASE_URL)
    }
    fun getMovieList(url: String) = viewModelScope.launch(Dispatchers.IO) {
        _movieListState.postValue(Resource.Loading)
        val result = repository.getMovieList(url)
        _movieListState.postValue(result)
    }
}