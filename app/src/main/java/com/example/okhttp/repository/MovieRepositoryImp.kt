package com.example.okhttp.repository

import API_KEY
import LANGUAGE
import com.example.okhttp.api.RetrofitService
import com.example.okhttp.models.MovieDetails
import com.example.okhttp.utils.Resource
import com.google.gson.Gson
import okhttp3.OkHttpClient
import okhttp3.Request
import java.lang.Exception
import java.net.URL
import javax.inject.Inject

class MovieRepositoryImp @Inject constructor(): MovieRepository{

    private val service = RetrofitService()
    override suspend fun getMovie(movieId: Int): MovieDetails = service.getMovie(movieId)
    override suspend fun getMovieList(page: Int) = service.getMovieList(page)

}