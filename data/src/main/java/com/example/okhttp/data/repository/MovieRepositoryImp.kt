package com.example.okhttp.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.core.utils.CommonResult
import com.example.okhttp.data.api.MovieApi
import com.example.okhttp.domain.model.ListItem
import com.example.okhttp.domain.model.MovieDetails
import com.example.okhttp.domain.model.MovieIsSaved
import com.example.okhttp.domain.repository.MovieRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class MovieRepositoryImp @Inject constructor(
    private val api: MovieApi
) : MovieRepository {
    override suspend fun getMovie(movieId: Int): CommonResult<MovieDetails> =
        withContext(Dispatchers.IO) {
            val response = api.getMovie(movieId)
            if (response.isSuccessful) {
                CommonResult(result = response.body()?.toDomain())
            } else {
                CommonResult(error = response.message())
            }
        }

    override suspend fun getIsMovieSaved(movieId: Int): CommonResult<MovieIsSaved> =
        withContext(Dispatchers.IO) {
            val response = api.getMovieIsSaved(movieId)
            if (response.isSuccessful) {
                CommonResult(result = response.body()?.toDomain())
            } else {
                CommonResult(error = response.message())
            }
    }

    override fun getPagedMovieList(): Flow<PagingData<ListItem>> {
        return Pager(PagingConfig(pageSize = 10, initialLoadSize = 10)) {
            MoviePagingSource(api)
        }.flow
    }
}
