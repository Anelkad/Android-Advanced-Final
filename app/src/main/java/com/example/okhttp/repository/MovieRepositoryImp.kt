package com.example.okhttp.repository

import API_KEY
import LANGUAGE
import android.util.Log
import com.example.okhttp.api.RetrofitService
import com.example.okhttp.models.Movie
import com.example.okhttp.models.MovieDetails
import com.example.okhttp.models.MovieListResponse
import com.example.okhttp.utils.Resource
import com.google.gson.Gson
import okhttp3.OkHttpClient
import okhttp3.Request
import java.lang.Exception
import java.net.URL
import javax.inject.Inject

class MovieRepositoryImp @Inject constructor(): MovieRepository{

    private val service = RetrofitService()
    override fun getRequest(url: String): String? {
            val client = OkHttpClient()
            var result: String? = null
            try {
                val request = Request
                    .Builder()
                    .url(URL(url))
                    .build()
                val response = client.newCall(request).execute()
                result = response.body?.string()
            } catch (err: Error) {
                print("Error when executing get request: " + err.localizedMessage)
            }
            return result
        }

    override fun getMovie(movieId: Int): Resource<MovieDetails> {
        var resource: Resource<MovieDetails> = Resource.Loading
        val url = "https://api.themoviedb.org/3/movie/${movieId}?api_key=${API_KEY}&language=${LANGUAGE}"

        val result = getRequest(url)
        if (result != null) {
            resource = try {
                val movieDetails = Gson()
                    .fromJson(result, MovieDetails::class.java)
                Resource.Success(movieDetails)
            } catch (e: Exception) {
                e.printStackTrace()
                Resource.Failure(e)
            }
        }
        return resource
    }

    override suspend fun getMovieList(page: Int) = service.getMovieList(page)

}