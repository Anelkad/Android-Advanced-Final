package com.example.okhttp.data.modelDTO

import com.google.gson.annotations.SerializedName

data class NewTokenResponse (
    val success: Boolean,
    @SerializedName("expires_at") val expiresAt: String,
    @SerializedName("request_token") val token: String
)