package com.example.okhttp.models

class MoviePage(
    val page: Int,
    val results: ArrayList<MovieItem>,
    val total_pages: Int
)
