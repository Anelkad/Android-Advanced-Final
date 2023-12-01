package com.example.data.repository

import MOVIES
import com.example.core.utils.CommonResult
import com.example.domain.model.Movie
import com.example.domain.repository.SavedMovieRepository
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class SavedMovieRepositoryImp @Inject constructor(
    private val firebase: FirebaseDatabase
) : SavedMovieRepository {
    override fun getSavedMovieList(): Flow<CommonResult<ArrayList<Movie>>> = callbackFlow {
        val movieList = ArrayList<Movie>()

        val postListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                movieList.clear()
                for (ds in snapshot.children) {
                    val movie = ds.getValue(com.example.data.modelDTO.MovieDTO::class.java)
                    if (movie != null) {
                        movieList.add(movie.toDomain())
                    }
                }
                this@callbackFlow.trySendBlocking(CommonResult(result = movieList))
            }

            override fun onCancelled(error: DatabaseError) {
                this@callbackFlow.trySendBlocking(CommonResult(error = "Error"))
            }
        }

        firebase.getReference(MOVIES)
            .addValueEventListener(postListener)

        awaitClose {
            firebase.getReference(MOVIES)
                .removeEventListener(postListener)
        }
    }

    override suspend fun deleteMovie(movieId: Int): Int {
        firebase.getReference(MOVIES).child(movieId.toString())
            .removeValue()
            .await()
        return movieId
    }

    override suspend fun saveMovie(movie: Movie): Movie {
        val database = firebase.getReference(MOVIES)
        database.child(movie.id.toString())
            .setValue(movie)
            .await()
        return movie
    }
}