package com.example.okhttp.repository

import androidx.paging.PagingData
import com.example.domain.model.ListItem
import com.example.domain.model.MovieDetails
import com.example.okhttp.utils.CommonResult
import kotlinx.coroutines.flow.Flow

interface MovieRepository {
    suspend fun getMovie(movieId: Int): CommonResult<com.example.domain.model.MovieDetails>
    fun getPagedMovieList(): Flow<PagingData<com.example.domain.model.ListItem>>
}