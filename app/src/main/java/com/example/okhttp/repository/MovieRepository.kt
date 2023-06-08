package com.example.okhttp.repository

import com.example.okhttp.models.Movie
import com.example.okhttp.models.MovieDetails
import com.example.okhttp.utils.Resource

interface MovieRepository {
    fun getRequest(url: String): String?
    fun getMovie(movieId: Int): Resource<MovieDetails>
    suspend fun getMovieList(page: Int): ArrayList<Movie>
}