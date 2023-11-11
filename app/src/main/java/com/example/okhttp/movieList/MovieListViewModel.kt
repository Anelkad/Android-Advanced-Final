package com.example.okhttp.movieList

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.okhttp.models.ListItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class MovieListViewModel @Inject constructor(
    private val movieUseCase: MovieUseCase
) : ViewModel() {

    val pagedMovieList: Flow<PagingData<ListItem>> =
        movieUseCase.getPagedMovieList().cachedIn(viewModelScope)

}