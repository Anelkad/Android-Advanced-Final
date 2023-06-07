package com.example.okhttp.repository

import API_KEY
import LANGUAGE
import android.util.Log
import com.example.okhttp.models.Movie
import com.example.okhttp.models.MoviePage
import com.example.okhttp.utils.Resource
import com.google.gson.Gson
import okhttp3.OkHttpClient
import okhttp3.Request
import java.lang.Exception
import java.net.URL
import javax.inject.Inject

class MovieRepositoryImp @Inject constructor(): MovieRepository{
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

    override fun getMovie(movieId: Int): Resource<Movie> {
        var resource: Resource<Movie> = Resource.Loading
        val sUrl = "https://api.themoviedb.org/3/movie/${movieId}?api_key=${API_KEY}&language=${LANGUAGE}"

        val result = getRequest(sUrl)
        if (result != null) {
            resource = try {
                val movie = Gson()
                    .fromJson(result, Movie::class.java)
                Resource.Success(movie)
            } catch (e: Exception) {
                e.printStackTrace()
                Resource.Failure(e)
            }

        }
        return resource
    }

    override fun getMovieList(url: String): Resource<MoviePage> {
        var resource: Resource<MoviePage> = Resource.Loading

        val result = getRequest(url)
        if (result != null) {
            resource = try {
                val moviePage = Gson()
                    .fromJson(result, MoviePage::class.java)
                Log.d("qwerty page: ", moviePage.page.toString())
                Resource.Success(moviePage)
            } catch (e: Exception) {
                e.printStackTrace()
                Resource.Failure(e)
            }
        }
        return resource
    }

}