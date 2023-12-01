package com.example.data.repository

import androidx.paging.PagingSource
import androidx.paging.PagingState

class MoviePagingSource(
    private val api: com.example.data.api.MovieApi
) : PagingSource<Int, com.example.domain.model.ListItem>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, com.example.domain.model.ListItem> {

        return try {
            val nextPage = params.key ?: 1
            val movieListResponse = api.getMovieList(nextPage)

            val ad = com.example.domain.model.Ad(
                "1Fit",
                "Абонемент на все виды спорта",
                "https://resources.cdn-kaspi.kz/shop/medias/sys_master/images/images/h4b/hf7/47592727773214/1fit-bezlimit-3-mesaca-101420202-1-Container.png"
            )

            val list = buildList {
                addAll(movieListResponse.results.map { com.example.domain.model.ListItem.MovieItem(it.toDomain()) })
                //каждые 10 фильмов - реклама
                add(10, com.example.domain.model.ListItem.AdItem(ad))
                add(21, com.example.domain.model.ListItem.AdItem(ad))
            }

            LoadResult.Page(
                data = list,
                prevKey = if (nextPage == 1) null else nextPage - 1,
                nextKey = if (nextPage < movieListResponse.totalPages)
                    movieListResponse.page.plus(1) else null
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, com.example.domain.model.ListItem>): Int? {
        return 1
    }
}
