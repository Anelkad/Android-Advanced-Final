package com.example.okhttp.data.modelDTO

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class LoginRequestBody(
    @SerializedName("username") val username: String,
    @SerializedName("password") val password: String,
    @SerializedName("request_token") val token: String?
) : Serializable