package com.example.okhttp.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.okhttp.api.MovieApi
import com.example.okhttp.models.ListItem
import com.example.okhttp.models.MovieDetails
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class MovieRepositoryImp @Inject constructor(
    private val api: MovieApi
) : MovieRepository {
    override suspend fun getMovie(movieId: Int): MovieDetails = api.getMovie(movieId)
    override fun getPagedMovieList(): Flow<PagingData<ListItem>> {
        return Pager(PagingConfig(pageSize = 10, initialLoadSize = 10)) {
            MoviePagingSource(api)
        }.flow
    }
}
