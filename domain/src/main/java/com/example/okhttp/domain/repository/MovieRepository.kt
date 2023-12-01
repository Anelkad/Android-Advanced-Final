package com.example.okhttp.domain.repository

import androidx.paging.PagingData
import com.example.core.utils.CommonResult
import com.example.okhttp.domain.model.ListItem
import com.example.okhttp.domain.model.MovieDetails
import kotlinx.coroutines.flow.Flow

interface MovieRepository {
    suspend fun getMovie(movieId: Int): CommonResult<MovieDetails>
    fun getPagedMovieList(): Flow<PagingData<ListItem>>
}