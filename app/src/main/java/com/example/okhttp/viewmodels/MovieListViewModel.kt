package com.example.okhttp.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.example.okhttp.models.Movie
import com.example.okhttp.models.MovieListResponse
import com.example.okhttp.repository.MovieRepository
import com.example.okhttp.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import javax.inject.Named

@HiltViewModel
class MovieListViewModel @Inject constructor(
    private val repository: MovieRepository
) : ViewModel() {
    //todo заменить LiveData на StateFlow

    private val _movieListState = MutableLiveData<Resource<MovieListResponse>>(null)
    val movieListState: LiveData<Resource<MovieListResponse>> =_movieListState

    val pagedMovieList: Flow<PagingData<Movie>> = repository.getPagedMovieList()
    init {
        getMovieList(1)
    }
    fun getMovieList(page: Int) = viewModelScope.launch(Dispatchers.IO) {
        _movieListState.postValue(Resource.Loading)
        val result = repository.getMovieList(page)
        _movieListState.postValue(Resource.Success(result))
    }

}