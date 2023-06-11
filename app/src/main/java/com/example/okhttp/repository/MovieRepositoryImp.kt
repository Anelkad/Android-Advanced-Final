package com.example.okhttp.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.okhttp.api.RetrofitService
import com.example.okhttp.models.Movie
import com.example.okhttp.models.MovieDetails
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

//пример без использования @Inject в MovieRepositoryImp
class MovieRepositoryImp(
    val service: RetrofitService
) : MovieRepository {
    override suspend fun getMovie(movieId: Int): MovieDetails = service.getMovie(movieId)
    override fun getPagedMovieList(): Flow<PagingData<Movie>> {
        return Pager(PagingConfig(pageSize = 10, initialLoadSize = 10)) {
            MoviePagingSource(service)
        }.flow
    }

}