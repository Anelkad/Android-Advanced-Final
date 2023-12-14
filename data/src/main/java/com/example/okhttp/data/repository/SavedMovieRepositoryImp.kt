package com.example.okhttp.data.repository

import com.example.core.utils.CommonResult
import com.example.okhttp.data.api.MovieApi
import com.example.okhttp.data.local.SessionManager
import com.example.okhttp.data.modelDTO.AddFavoriteMovieRequest
import com.example.okhttp.domain.model.Movie
import com.example.okhttp.domain.repository.SavedMovieRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SavedMovieRepositoryImp @Inject constructor(
    private val api: MovieApi,
    private val sessionManager: SessionManager
) : SavedMovieRepository {
    override fun getSavedMovieList(): Flow<CommonResult<List<Movie>>> = flow {
        val response = api.getFavoriteMovieList(userId = sessionManager.uid.toInt())
        if (response.isSuccessful) {
            emit(CommonResult(result = response.body()?.results?.map { it.toDomain() }))
        } else {
            emit(CommonResult(error = response.message()))
        }
    }


    override suspend fun deleteMovie(movieId: Int) = withContext(Dispatchers.IO) {
        val response = api.addFavoriteMovie(
            body = AddFavoriteMovieRequest(
                movieId = movieId,
                isFavorite = false
            ),
            userId = sessionManager.uid.toInt()
        )
        if (response.isSuccessful) {
            CommonResult(result = response.body()?.toDomain())
        } else {
            CommonResult(error = response.message())
        }
    }

    override suspend fun saveMovie(movieId: Int) = withContext(Dispatchers.IO) {
        val response = api.addFavoriteMovie(
            body = AddFavoriteMovieRequest(
                movieId = movieId,
                isFavorite = true
            ),
            userId = sessionManager.uid.toInt()
        )
        if (response.isSuccessful) {
            CommonResult(result = response.body()?.toDomain())
        } else {
            CommonResult(error = response.message())
        }
    }
}