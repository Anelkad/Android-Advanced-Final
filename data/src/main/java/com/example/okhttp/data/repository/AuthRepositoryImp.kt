package com.example.okhttp.data.repository

import android.content.SharedPreferences
import com.example.core.utils.CommonResult
import com.example.okhttp.data.api.AuthApi
import com.example.okhttp.data.local.SessionManager
import com.example.okhttp.data.modelDTO.LoginRequest
import com.example.okhttp.domain.repository.AuthRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


class AuthRepositoryImp(
    private val sessionManager: SessionManager,
    private val sharedPrefs: SharedPreferences,
    private val api: AuthApi
) : AuthRepository {

    override suspend fun login(username: String, password: String): CommonResult<Boolean>? =
        withContext(Dispatchers.IO) {
            val newTokenResponse = getNewToken()
            newTokenResponse.result?.let { token ->
                val response = api.login(
                    body = LoginRequest(
                        user = username,
                        password = password,
                        token = token
                    )
                )
                if (response.isSuccessful) {
//            sessionManager.updateSession(token, uid)
                    sessionManager.saveUsername(username)
                    CommonResult(
                        result = response.body()?.success
                    )
                } else {
                    CommonResult(
                        error = response.message()
                    )
                }
            }
        }

    override suspend fun getNewToken(): CommonResult<String> =
        withContext(Dispatchers.IO) {
            val response = api.getNewToken()
            if (response.isSuccessful) {
                sessionManager.saveToken(response.body()?.token)
                CommonResult(
                    result = response.body()?.token
                )
            } else {
                CommonResult(error = response.message())
            }
        }

    override suspend fun getNewSession(): CommonResult<String> = withContext(Dispatchers.IO) {
        val response = api.getNewSession()
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

    override fun clearSession() {
        sessionManager.clearSession()
    }

}