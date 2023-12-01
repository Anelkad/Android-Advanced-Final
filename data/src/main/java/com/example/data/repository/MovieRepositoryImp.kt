package com.example.okhttp.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.data.api.MovieApi
import com.example.domain.model.ListItem
import com.example.domain.model.MovieDetails
import com.example.okhttp.utils.CommonResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class MovieRepositoryImp @Inject constructor(
    private val api: com.example.data.api.MovieApi
) : MovieRepository {
    override suspend fun getMovie(movieId: Int): CommonResult<com.example.domain.model.MovieDetails> = withContext(Dispatchers.IO) {
        val response = api.getMovie(movieId)
        if (response.isSuccessful) {
            CommonResult(result = response.body()?.toDomain())
        } else {
            CommonResult(error = response.message())
        }
    }

    override fun getPagedMovieList(): Flow<PagingData<com.example.domain.model.ListItem>> {
        return Pager(PagingConfig(pageSize = 10, initialLoadSize = 10)) {
            MoviePagingSource(api)
        }.flow
    }
}
