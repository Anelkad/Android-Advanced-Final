package com.example.okhttp.domain.usecases

import androidx.paging.PagingData
import com.example.core.utils.CommonResult
import com.example.okhttp.domain.model.MovieDetails
import com.example.okhttp.domain.repository.MovieRepository
import com.example.okhttp.domain.model.ListItem
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class MovieUseCase @Inject constructor(
    private val movieRepository: MovieRepository,
) {

    fun getPagedMovieList(): Flow<PagingData<ListItem>> =
        movieRepository.getPagedMovieList()

    suspend fun getMovie(movieId: Int): CommonResult<MovieDetails> = movieRepository.getMovie(movieId)

}