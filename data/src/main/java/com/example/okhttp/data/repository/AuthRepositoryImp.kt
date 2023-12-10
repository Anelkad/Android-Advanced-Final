package com.example.okhttp.data.repository

import com.example.core.utils.CommonResult
import com.example.okhttp.data.api.AuthApi
import com.example.okhttp.data.api.MovieApi
import com.example.okhttp.data.local.SessionManager
import com.example.okhttp.data.modelDTO.LoginRequestBody
import com.example.okhttp.data.modelDTO.SessionRequestBody
import com.example.okhttp.domain.repository.AuthRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


class AuthRepositoryImp(
    private val sessionManager: SessionManager,
    private val authApi: AuthApi,
    private val movieApi: MovieApi
) : AuthRepository {

    override suspend fun login(username: String, password: String): CommonResult<Boolean>? =
        withContext(Dispatchers.IO) {
            if (sessionManager.isAccessTokenEmpty()) getNewToken()
            val response = authApi.login(
                body = LoginRequestBody(
                    username = username,
                    password = password,
                    token = sessionManager.token
                )
            )
            if (response.isSuccessful) {
                val getSessionResponse = getNewSession()
                getSessionResponse.result?.let {
                    getUserId()
                    CommonResult(
                        result = response.body()?.success
                    )
                }
            } else {
                CommonResult(
                    error = response.message()
                )
            }
        }

override suspend fun getNewToken(): CommonResult<String> =
    withContext(Dispatchers.IO) {
        val response = authApi.getNewToken()
        if (response.isSuccessful) {
            sessionManager.saveToken(response.body()?.token)
            CommonResult(
                result = response.body()?.token
            )
        } else {
            CommonResult(error = response.message())
        }
    }

override suspend fun getUserId(): CommonResult<String> =
    withContext(Dispatchers.IO) {
        val response = movieApi.getUserId()
        if (response.isSuccessful) {
            sessionManager.saveUser(
                username = response.body()?.username,
                id = response.body()?.id
            )
            CommonResult(
                result = response.body()?.id
            )
        } else {
            CommonResult(error = response.message())
        }
    }

override suspend fun getNewSession(): CommonResult<String> = withContext(Dispatchers.IO) {
    val response = authApi.getNewSession(
        body = SessionRequestBody(token = sessionManager.token)
    )
    if (response.isSuccessful) {
        sessionManager.saveSession(response.body()?.session)
        CommonResult(
            result = response.body()?.session
        )
    } else {
        CommonResult(error = response.message())
    }
}

override fun getUid(): String = sessionManager.uid

override fun getToken(): String = sessionManager.token

override fun getUsername(): String = sessionManager.username

override fun getSession(): String = sessionManager.session

override fun isAccessTokenEmpty(): Boolean = sessionManager.isAccessSessionEmpty()
override fun isAccessSessionEmpty(): Boolean = sessionManager.isAccessSessionEmpty()

override fun clearSession() {
    sessionManager.clearSession()
}

}