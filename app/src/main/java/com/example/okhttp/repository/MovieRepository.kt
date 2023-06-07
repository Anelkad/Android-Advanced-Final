package com.example.okhttp.repository

import com.example.okhttp.models.Movie
import com.example.okhttp.models.MoviePage
import com.example.okhttp.utils.Resource

interface MovieRepository {
    fun getRequest(url: String): String?
    fun getMovie(movieId: Int): Resource<Movie>
    fun getMovieList(url: String): Resource<MoviePage>
}