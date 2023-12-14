package com.example.okhttp.domain.usecases

import com.example.okhttp.domain.repository.AuthRepository
import javax.inject.Inject

class GetUserPrefsUseCase @Inject constructor(
    private val authRepository: AuthRepository,
) {
    fun getUid() = authRepository.getUid()
    fun getUsername() = authRepository.getUsername()
    fun getToken() = authRepository.getToken()
    fun getSession() = authRepository.getSession()
    fun isAccessSessionEmpty() = authRepository.isAccessSessionEmpty()
    fun isAccessTokenEmpty() = authRepository.isAccessTokenEmpty()
}