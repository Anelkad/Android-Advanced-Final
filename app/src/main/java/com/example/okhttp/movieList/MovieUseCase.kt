package com.example.okhttp.movieList

import androidx.paging.PagingData
import com.example.okhttp.models.ListItem
import com.example.okhttp.models.MovieDetails
import com.example.okhttp.repository.MovieRepository
import com.example.okhttp.utils.CommonResult
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class MovieUseCase @Inject constructor(
    private val movieRepository: MovieRepository,
) {

    fun getPagedMovieList(): Flow<PagingData<ListItem>> =
        movieRepository.getPagedMovieList()

    suspend fun getMovie(movieId: Int): CommonResult<MovieDetails> = movieRepository.getMovie(movieId)

}