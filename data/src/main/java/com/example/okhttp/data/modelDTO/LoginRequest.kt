package com.example.okhttp.data.modelDTO

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class LoginRequest(
    @SerializedName("user") val user: String,
    @SerializedName("password") val password: String
) : Serializable