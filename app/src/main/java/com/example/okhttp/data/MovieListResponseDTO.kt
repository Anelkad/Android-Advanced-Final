package com.example.okhttp.data

import com.google.gson.annotations.SerializedName

data class MovieListResponseDTO(
    val page: Int,
    val results: List<MovieDTO>,
    @SerializedName("total_pages")
    val totalPages: Int
)