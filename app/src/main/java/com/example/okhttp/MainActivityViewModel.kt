package com.example.okhttp

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import com.beust.klaxon.Klaxon
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import java.net.URL

fun getRequest(sUrl: String): String? {

    // Create OkHttp Client
    var client = OkHttpClient();

    var result: String? = null
    try {
        val url = URL(sUrl)
        val request = Request.Builder().url(url).build()

        val response = client.newCall(request).execute()
        result = response.body?.string()
        Log.d("qwerty: ", result!!)
    }
    catch(err:Error) {
        print("Error when executing get request: "+err.localizedMessage)
    }
    return result
}
class MovieListViewModel : ViewModel() {
    val page = MutableLiveData<Int>()
    val results = MutableLiveData<List<MovieItem>>()

    fun fetchList(sUrl: String): MovieList? {

        var movieList: MovieList? = null

        viewModelScope.launch(Dispatchers.IO) {
            val result = getRequest(sUrl)
            if (result != null) {
                try {
                    // Parse result string JSON to data class
                    movieList = Klaxon().parse<MovieList>(result)

                    withContext(Dispatchers.Main) {
                        // Update view model
                        page.value = movieList?.page
                        results.value = movieList?.results
                    }
                }
                catch(err:Error) {
                    print("Error when parsing JSON: "+err.localizedMessage)
                }
            }
            else {
                print("Error: Get request returned no response")
            }
        }
        return movieList
    }
}

class MovieViewModel : ViewModel() {
    val id = MutableLiveData<Int>()
    val title = MutableLiveData<String>()
    val overview = MutableLiveData<String>()
    val release_date = MutableLiveData<String>()
    val poster_path = MutableLiveData<String>()
    val backdrop_path = MutableLiveData<String>()
    val tagline = MutableLiveData<String>()
    val vote_average = MutableLiveData<Float>()
    val runtime = MutableLiveData<Int>()
    val revenue = MutableLiveData<Int>()
    fun fetchMovie(sUrl: String): Movie? {
        var movie: Movie? = null

        viewModelScope.launch(Dispatchers.IO) {
            val result = getRequest(sUrl)
            if (result != null) {
                try {
                    // Parse result string JSON to data class
                    movie = Klaxon().parse<Movie>(result)

                    withContext(Dispatchers.Main) {
                        // Update view model
                        title.value = movie?.title
                        overview.value = movie?.overview
                        release_date.value = movie?.release_date
                        vote_average.value = movie?.vote_average
                        poster_path.value = movie?.poster_path
                        backdrop_path.value = movie?.backdrop_path
                        tagline.value = movie?.tagline
                        runtime.value = movie?.runtime
                        revenue.value = movie?.revenue
                    }
                }
                catch(err:Error) {
                    print("Error when parsing JSON: "+err.localizedMessage)
                }
            }
            else {
                print("Error: Get request returned no response")
            }
        }
        return movie
    }

}