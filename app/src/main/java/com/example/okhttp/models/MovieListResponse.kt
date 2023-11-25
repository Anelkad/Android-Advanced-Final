package com.example.okhttp.models

import com.google.gson.annotations.SerializedName

data class MovieListResponse(
    val page: Int = 0,
    val results: List<Movie> = emptyList(),
    @SerializedName("total_pages")
    val totalPages: Int = 0
)
