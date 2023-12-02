package com.example.data.modelDTO

import com.example.domain.model.Movie
import com.google.gson.annotations.SerializedName

data class MovieDTO(
    var id: Int = 0,
    var title: String = "",
    var overview: String? = null,
    @SerializedName("release_date")
    var releaseDate: String? = null,
    @SerializedName("poster_path")
    var posterPath: String? = null,
    @SerializedName("backdrop_path")
    var backdropPath: String? = null,
    @SerializedName("vote_average")
    var voteAverage: Float? = null
){
    //todo remove no-arg constructor
    fun toDomain(): Movie =
        Movie(
            id = id,
            title = title,
            overview = overview?:"",
            releaseDate = releaseDate?:"",
            posterPath = posterPath?:"",
            backdropPath = backdropPath?:"",
            voteAverage = voteAverage?:0F
        )
}
