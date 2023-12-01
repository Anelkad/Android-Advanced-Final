package com.example.okhttp.movieList

import androidx.paging.PagingData
import com.example.domain.repository.MovieRepository
import com.example.okhttp.utils.CommonResult
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class MovieUseCase @Inject constructor(
    private val movieRepository: MovieRepository,
) {

    fun getPagedMovieList(): Flow<PagingData<com.example.domain.model.ListItem>> =
        movieRepository.getPagedMovieList()

    suspend fun getMovie(movieId: Int): CommonResult<com.example.domain.model.MovieDetails> = movieRepository.getMovie(movieId)

}