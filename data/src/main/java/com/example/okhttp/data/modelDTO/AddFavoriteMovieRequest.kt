package com.example.okhttp.data.modelDTO

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class AddFavoriteMovieRequest(
    @SerializedName("media_type")
    val mediaType: String = "movie",
    @SerializedName("media_id")
    val movieId: Int,
    @SerializedName("favorite")
    val isFavorite: Boolean
) : Serializable
