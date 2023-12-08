package com.example.okhttp.domain.usecases

import com.example.core.utils.CommonResult
import com.example.okhttp.domain.model.AddFavoriteMovieResponse
import com.example.okhttp.domain.repository.SavedMovieRepository
import javax.inject.Inject

class SaveMovieUseCase @Inject constructor(
    private val savedMovieRepository: SavedMovieRepository,
) {
    suspend fun deleteMovie(movieId: Int): CommonResult<AddFavoriteMovieResponse> = savedMovieRepository.deleteMovie(movieId)

    suspend fun saveMovie(movieId: Int): CommonResult<AddFavoriteMovieResponse> = savedMovieRepository.saveMovie(movieId)

}
