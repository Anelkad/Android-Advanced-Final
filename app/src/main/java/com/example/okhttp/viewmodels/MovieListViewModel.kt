package com.example.okhttp.viewmodels

import androidx.lifecycle.*
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.okhttp.models.Movie
import com.example.okhttp.models.MovieListResponse
import com.example.okhttp.repository.MovieRepository
import com.example.okhttp.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class MovieListViewModel @Inject constructor(
    private val repository: MovieRepository
) : ViewModel() {
    //todo заменить LiveData на StateFlow

    val pagedMovieList: Flow<PagingData<Movie>> =
        repository.getPagedMovieList().cachedIn(viewModelScope)
}