package com.example.domain.usecases

import androidx.paging.PagingData
import com.example.core.utils.CommonResult
import com.example.domain.model.MovieDetails
import com.example.domain.repository.MovieRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class MovieUseCase @Inject constructor(
    private val movieRepository: MovieRepository,
) {

    fun getPagedMovieList(): Flow<PagingData<com.example.domain.model.ListItem>> =
        movieRepository.getPagedMovieList()

    suspend fun getMovie(movieId: Int): CommonResult<MovieDetails> = movieRepository.getMovie(movieId)

}