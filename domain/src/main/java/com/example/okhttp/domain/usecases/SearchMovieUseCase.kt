package com.example.okhttp.domain.usecases

import com.example.core.utils.CommonResult
import com.example.okhttp.domain.model.Movie
import com.example.okhttp.domain.repository.MovieRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SearchMovieUseCase @Inject constructor(
    private val movieRepository: MovieRepository,
) {
    fun searchMovie(query: String): Flow<CommonResult<List<Movie>>> = movieRepository.searchMovie(query)
}
