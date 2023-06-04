package com.example.okhttp

import API_KEY
import BASE_URL
import FIREBASE_URL
import LANGUAGE
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.okhttp.models.Movie
import com.example.okhttp.models.MovieItem
import com.example.okhttp.models.MoviePage
import com.google.firebase.database.*
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Request
import java.net.URL

fun getRequest(sUrl: String): String? {

    val client = OkHttpClient();
    var result: String? = null
    try {
        val url = URL(sUrl)
        val request = Request.Builder().url(url).build()
        val response = client.newCall(request).execute()
        result = response.body?.string()
    } catch (err: Error) {
        print("Error when executing get request: " + err.localizedMessage)
    }
    return result
}

class MovieListViewModel : ViewModel() {
    var page = MutableLiveData<Int>()
    var results = MutableLiveData<ArrayList<MovieItem>>()
    var total_pages = MutableLiveData<Int>()

    init {
        fetchList(BASE_URL)
    }

    fun fetchList(sUrl: String) {
        var moviePage: MoviePage? = null

        viewModelScope.launch(Dispatchers.IO) {
            val result = getRequest(sUrl)
            if (result != null) {
                try {
                    moviePage = Gson().fromJson(result, MoviePage::class.java)
                    page.postValue(moviePage?.page)
                    results.postValue(moviePage?.results)
                    total_pages.postValue(moviePage?.total_pages)
                    Log.d("page: ", moviePage?.page.toString())
                } catch (err: Error) {
                    print("Error when parsing JSON: " + err.localizedMessage)
                }
            } else {
                print("Error: Get request returned no response")
            }
        }
    }
}

class SavedMovieListViewModel : ViewModel() {
    var results = MutableLiveData<ArrayList<MovieItem>>()
    lateinit var database: DatabaseReference

    init {
        fetchList()
    }

    fun fetchList() {
        val movieList = ArrayList<MovieItem>()

        viewModelScope.launch(Dispatchers.Main) {
            database = FirebaseDatabase.getInstance(FIREBASE_URL).getReference("movies")

            database.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    movieList.clear()
                    for (ds in snapshot.children) {
                        val movie = ds.getValue(MovieItem::class.java)
                        if (movie != null) {
                            movieList.add(movie)
                            Log.d("movie add", movie.title)
                        }
                    }
                    results.value = movieList
                }

                override fun onCancelled(error: DatabaseError) {

                }
            })
        }
    }

    fun deleteMovie(id: Int, context: Context) {
        viewModelScope.launch(Dispatchers.Main) {
            database = FirebaseDatabase.getInstance(FIREBASE_URL).getReference("movies")

            database.child(id.toString())
                .removeValue()
                .addOnSuccessListener {
                    Toast.makeText(
                        context, "Movie deleted!",
                        Toast.LENGTH_LONG
                    ).show()
                }
                .addOnFailureListener { e ->
                    Log.d("book delete", "Error: ${e.message}")
                    Toast.makeText(
                        context, "Cannot delete movie!",
                        Toast.LENGTH_LONG
                    ).show()
                }
        }
    }

    fun addMovie(movie: MovieItem, context: Context) {
        viewModelScope.launch(Dispatchers.Main) {
            database = FirebaseDatabase.getInstance(FIREBASE_URL).getReference("movies")
            database.child(movie.id.toString()).setValue(movie)
                .addOnSuccessListener {
                    Toast.makeText(
                        context, "Movie: \"${movie.title}\" saved!",
                        Toast.LENGTH_LONG
                    ).show()
                }
                .addOnFailureListener { e ->
                    Log.d("Movie saving", "Error: ${e.message}")
                    Toast.makeText(
                        context, "Cannot save movie!",
                        Toast.LENGTH_LONG
                    ).show()
                }
        }
    }
}

class MovieViewModel : ViewModel() {
    var movie = MutableLiveData<Movie?>()
    fun fetchMovie(movieId: Int) {
        val sUrl = "https://api.themoviedb.org/3/movie/${movieId}?api_key=${API_KEY}&language=${LANGUAGE}"

        viewModelScope.launch(Dispatchers.IO) {
            val result = getRequest(sUrl)
            if (result != null) {
                try {
                    movie.postValue(Gson().fromJson(result, Movie::class.java))
                } catch (err: Error) {
                    print("Error when parsing JSON: " + err.localizedMessage)
                }
            } else {
                print("Error: Get request returned no response")
            }
        }
    }

}