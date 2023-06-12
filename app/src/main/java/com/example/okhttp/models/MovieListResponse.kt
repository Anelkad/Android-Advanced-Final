package com.example.okhttp.models

import com.google.gson.annotations.SerializedName

data class MovieListResponse(
    val page: Int,
    val results: ArrayList<Movie>,
    @SerializedName("total_pages")
    val totalPages: Int
)
