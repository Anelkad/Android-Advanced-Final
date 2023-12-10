package com.example.okhttp.data.modelDTO

import com.google.gson.annotations.SerializedName

data class SessionRequestBody(
    @SerializedName("request_token") val token: String
)