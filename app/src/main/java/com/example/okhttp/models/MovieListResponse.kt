package com.example.okhttp.models

data class MovieListResponse(
    val page: Int,
    val results: ArrayList<Movie>,
    val total_pages: Int
)
