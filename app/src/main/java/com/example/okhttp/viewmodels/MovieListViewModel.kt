package com.example.okhttp.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.okhttp.models.Movie
import com.example.okhttp.repository.MovieRepository
import com.example.okhttp.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieListViewModel @Inject constructor(
    private val repository: MovieRepository
) : ViewModel() {

    private val _movieListState = MutableLiveData<Resource<ArrayList<Movie>>>(null)
    val movieListState: LiveData<Resource<ArrayList<Movie>>> =_movieListState
    init {
        getMovieList(1)
    }
    fun getMovieList(page: Int) = viewModelScope.launch(Dispatchers.IO) {
        _movieListState.postValue(Resource.Loading)
        val result = repository.getMovieList(page)
        _movieListState.postValue(Resource.Success(result))
    }
}