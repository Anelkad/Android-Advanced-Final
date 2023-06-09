package com.example.okhttp.repository

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.okhttp.api.RetrofitService
import com.example.okhttp.models.Movie

class MoviePagingSource(
    private val service: RetrofitService
) : PagingSource<Int, Movie>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Movie> {

        return try {
            val nextPage = params.key ?: 1
            val movieListResponse = service.getMovieList(nextPage)

            LoadResult.Page(
                data = movieListResponse.results,
                prevKey = if (nextPage == 1) null else nextPage - 1 ,
                nextKey = if (nextPage < movieListResponse.total_pages)
                    movieListResponse.page.plus(1) else null
            )
        }catch (e: Exception){
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Movie>): Int {
        return 1
    }
}
