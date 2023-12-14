package com.example.okhttp.data.modelDTO

import com.google.gson.annotations.SerializedName

data class LoginResponse(
    val success: Boolean,
    @SerializedName("status_code") val statusCode: Int,
    @SerializedName("status_message") val statusMessage: String
)