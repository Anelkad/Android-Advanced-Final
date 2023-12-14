package com.example.okhttp.data.modelDTO

import com.google.gson.annotations.SerializedName

data class NewSessionResponse (
    val success: Boolean,
    @SerializedName("session_id") val session: String
)