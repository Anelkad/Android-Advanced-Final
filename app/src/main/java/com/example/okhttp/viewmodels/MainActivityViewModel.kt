package com.example.okhttp

import BASE_URL
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.beust.klaxon.Klaxon
import com.example.okhttp.models.Movie
import com.example.okhttp.models.MovieItem
import com.example.okhttp.models.MoviePage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import java.net.URL

fun getRequest(sUrl: String): String? {

    var client = OkHttpClient();

    var result: String? = null
    try {
        val url = URL(sUrl)
        val request = Request.Builder().url(url).build()

        val response = client.newCall(request).execute()
        result = response.body?.string()
        //Log.d("qwerty: ", result!!)
    }
    catch(err:Error) {
        print("Error when executing get request: "+err.localizedMessage)
    }
    return result
}
class MovieListViewModel : ViewModel() {
    var page = MutableLiveData<Int>()
    var results = MutableLiveData<ArrayList<MovieItem>>()
    var total_pages = MutableLiveData<Int>()

    val loading = MutableLiveData<Boolean>()


    fun fetchList(sUrl: String){
        var moviePage: MoviePage? = null
        loading.postValue(true)

        viewModelScope.launch(Dispatchers.IO) {
            val result = getRequest(sUrl)
            if (result != null) {
                try {
                    // Parse result string JSON to data class
                    moviePage = Klaxon().parse<MoviePage>(result)

                    withContext(Dispatchers.Main) {
                        // Update view model
                        page.value = moviePage?.page
                        results.value = moviePage?.results
                        total_pages.value = moviePage?.total_pages
                        Log.d("page: ", moviePage?.page.toString())
                    }
                } catch (err: Error) {
                    print("Error when parsing JSON: " + err.localizedMessage)
                }
            } else {
                print("Error: Get request returned no response")
            }
        loading.postValue(false)
        }
    }
}

class MovieViewModel : ViewModel() {
    var id = MutableLiveData<Int>()
    var title = MutableLiveData<String>()
    var overview = MutableLiveData<String>()
    var release_date = MutableLiveData<String>()
    var poster_path = MutableLiveData<String>()
    var backdrop_path = MutableLiveData<String>()
    var tagline = MutableLiveData<String>()
    var vote_average = MutableLiveData<Float>()
    var runtime = MutableLiveData<Int>()
    var revenue = MutableLiveData<Int>()
    fun fetchMovie(sUrl: String){
        var movie: Movie? = null

        viewModelScope.launch(Dispatchers.IO) {
            val result = getRequest(sUrl)
            if (result != null) {
                try {
                    // Parse result string JSON to data class
                    movie = Klaxon().parse<Movie>(result)

                    withContext(Dispatchers.Main) {
                        // Update view model
                        id.value = movie?.id
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
    }

}