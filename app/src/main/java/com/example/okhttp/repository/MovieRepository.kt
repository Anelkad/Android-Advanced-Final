package com.example.okhttp.repository

import androidx.paging.PagingData
import com.example.okhttp.domain.ListItem
import com.example.okhttp.models.MovieDetails
import com.example.okhttp.utils.CommonResult
import kotlinx.coroutines.flow.Flow

interface MovieRepository {
    suspend fun getMovie(movieId: Int): CommonResult<MovieDetails>
    fun getPagedMovieList(): Flow<PagingData<ListItem>>
}