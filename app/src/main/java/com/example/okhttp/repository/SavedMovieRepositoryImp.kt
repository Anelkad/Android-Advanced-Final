package com.example.okhttp.repository

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.viewModelScope
import com.example.okhttp.models.MovieItem
import com.example.okhttp.utils.Resource
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

class SavedMovieRepositoryImp @Inject constructor(
    val firebase: FirebaseDatabase
): SavedMovieRepository {
    override fun getSavedMovieList(): Flow<Resource<ArrayList<MovieItem>>> = callbackFlow {
        val movieList = ArrayList<MovieItem>()

        val postListener = object: ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    movieList.clear()
                    for (ds in snapshot.children) {
                        val movie = ds.getValue(MovieItem::class.java)
                        if (movie != null) {
                            movieList.add(movie)
                        }
                    }
                    this@callbackFlow.trySendBlocking(Resource.Success(movieList))
                }

                override fun onCancelled(error: DatabaseError) {
                    this@callbackFlow.trySendBlocking(Resource.Failure(error.toException()))
                }
            }

        firebase.getReference("movies")
            .addValueEventListener(postListener)

        awaitClose{
            firebase.getReference("movies")
                .removeEventListener(postListener)
        }

    }

    override suspend fun deleteMovie(movieId: Int): Resource<Int> {
         firebase.getReference("movies").child(movieId.toString())
                .removeValue()
        return Resource.Success(movieId)
    }

    override suspend fun saveMovie(movie: MovieItem): Resource<MovieItem> {
        val database = firebase.getReference("movies")
            database.child(movie.id.toString())
                .setValue(movie)

        //todo почему await не ставиться
        return Resource.Success(movie)
    }
}