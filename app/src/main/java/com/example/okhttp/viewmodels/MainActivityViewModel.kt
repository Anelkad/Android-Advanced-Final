package com.example.okhttp

import BASE_URL
import FIREBASE_URL
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.okhttp.models.MovieItem
import com.example.okhttp.models.MoviePage
import com.example.okhttp.utils.Resource
import com.google.firebase.database.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

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
