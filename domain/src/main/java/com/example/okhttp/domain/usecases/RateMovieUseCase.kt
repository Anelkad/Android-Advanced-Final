package com.example.okhttp.domain.usecases

import com.example.core.utils.CommonResult
import com.example.okhttp.domain.model.AddMovieResponse
import com.example.okhttp.domain.repository.SavedMovieRepository
import javax.inject.Inject

class RateMovieUseCase @Inject constructor(
    private val savedMovieRepository: SavedMovieRepository,
) {
    suspend fun rateMovie(movieId: Int, rating: Double): CommonResult<AddMovieResponse> =
        savedMovieRepository.rateMovie(movieId = movieId, rating = rating)
}
