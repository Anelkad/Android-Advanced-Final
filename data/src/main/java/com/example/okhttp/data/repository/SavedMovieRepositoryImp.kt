package com.example.okhttp.data.repository

import com.example.core.utils.CommonResult
import com.example.okhttp.data.api.MovieApi
import com.example.okhttp.domain.model.Movie
import com.example.okhttp.domain.repository.SavedMovieRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SavedMovieRepositoryImp @Inject constructor(
    private val api: MovieApi
) : SavedMovieRepository {
    override suspend fun getSavedMovieList(): CommonResult<List<Movie>> =
        withContext(Dispatchers.IO) {
            val response = api.getFavoriteMovieList()
            if (response.isSuccessful) {
                CommonResult(result = response.body()?.results?.map { it.toDomain() })
            } else {
                CommonResult(error = response.message())
            }
        }


    override suspend fun deleteMovie(movieId: Int): Int {
//        firebase.getReference(MOVIES).child(movieId.toString())
//            .removeValue()
//            .await()
        return movieId
    }

    override suspend fun saveMovie(movie: Movie): Movie {
//        val database = firebase.getReference(MOVIES)
//        database.child(movie.id.toString())
//            .setValue(movie)
//            .await()
        return movie
    }
}