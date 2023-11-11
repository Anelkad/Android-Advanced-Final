package com.example.okhttp.movieList

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.okhttp.models.ListItem
import com.example.okhttp.models.Movie
import com.example.okhttp.savedMovieList.SavedMovieUseCase
import com.example.okhttp.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieListViewModel @Inject constructor(
    private val movieUseCase: MovieUseCase,
    private val savedMovieUseCase: SavedMovieUseCase
) : ViewModel() {

    private val _saveMovieState = MutableLiveData<Resource<Movie>>(null)
    val saveMovieState: LiveData<Resource<Movie>> = _saveMovieState

    val pagedMovieList: Flow<PagingData<ListItem>> =
        movieUseCase.getPagedMovieList().cachedIn(viewModelScope)
    fun saveMovie(movie: Movie) = viewModelScope.launch {
        _saveMovieState.value = Resource.Loading
        val result = savedMovieUseCase.saveMovie(movie)
        _saveMovieState.value = result
    }
}