package com.example.okhttp.repository

import androidx.paging.PagingData
import com.example.okhttp.models.Movie
import com.example.okhttp.models.MovieDetails
import com.example.okhttp.models.MovieListResponse
import com.example.okhttp.utils.Resource
import kotlinx.coroutines.flow.Flow

interface MovieRepository {
    suspend fun getMovie(movieId: Int): MovieDetails
    fun getPagedMovieList(): Flow<PagingData<Movie>>
}