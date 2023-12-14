package com.example.okhttp.data.modelDTO

import com.example.okhttp.domain.model.AddFavoriteMovieResponse
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class AddFavoriteMovieResponseDTO(
    @SerializedName("success") val success: Boolean,
    @SerializedName("status_code") val statusCode: Int,
    @SerializedName("status_message") val statusMessage: String
) : Serializable {
    fun toDomain(): AddFavoriteMovieResponse =
        AddFavoriteMovieResponse(
            success = this.success,
            statusCode = this.statusCode,
            statusMessage = this.statusMessage
        )
}