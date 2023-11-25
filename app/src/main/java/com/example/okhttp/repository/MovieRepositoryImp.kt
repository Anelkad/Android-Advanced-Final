package com.example.okhttp.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.okhttp.api.MovieApi
import com.example.okhttp.models.ListItem
import com.example.okhttp.models.MovieDetails
import com.example.okhttp.utils.CommonResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class MovieRepositoryImp @Inject constructor(
    private val api: MovieApi
) : MovieRepository {
    override suspend fun getMovie(movieId: Int): CommonResult<MovieDetails> = withContext(Dispatchers.IO) {
        val response = api.getMovie(movieId)
        if (response.isSuccessful) {
            CommonResult(result = response.body())
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
