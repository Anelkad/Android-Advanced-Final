package com.example.okhttp.repository

import com.example.okhttp.models.Movie
import com.example.okhttp.models.MovieDetails
import com.example.okhttp.models.MovieListResponse
import com.example.okhttp.utils.Resource

interface MovieRepository {
    suspend fun getMovie(movieId: Int): MovieDetails
    suspend fun getMovieList(page: Int): MovieListResponse
}