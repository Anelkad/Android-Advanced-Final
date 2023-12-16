package com.example.okhttp.data.modelDTO

import com.example.okhttp.domain.model.AddMovieResponse
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class AddMovieResponseDTO(
    @SerializedName("success") val success: Boolean,
    @SerializedName("status_code") val statusCode: Int,
    @SerializedName("status_message") val statusMessage: String
) : Serializable {
    fun toDomain(): AddMovieResponse =
        AddMovieResponse(
            success = this.success,
            statusCode = this.statusCode,
            statusMessage = this.statusMessage
        )
}