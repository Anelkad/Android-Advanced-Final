package com.example.okhttp.models

class MovieList(
    val page: Int,
    val results: List<MovieItem>,
    val total_pages: Int
)
