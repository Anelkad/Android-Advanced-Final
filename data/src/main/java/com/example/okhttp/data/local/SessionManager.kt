package com.example.okhttp.data.local

import ARG_EXPIRES_AT
import ARG_TOKEN
import ARG_UID
import ARG_USERNAME
import ENCRYPTED_SHARED_PREFERENCES
import android.content.SharedPreferences
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.util.*
import javax.inject.Inject
import javax.inject.Named

class SessionManager @Inject constructor(
    @Named(ENCRYPTED_SHARED_PREFERENCES) val sharedPreferences: SharedPreferences
) {

    var token: String = ""
        get() {
            return field.ifEmpty { sharedPreferences.getString(ARG_TOKEN, "") ?: "" }
        }
        set(value) {
            field = value
            _loggedOut.value = false
            sharedPreferences.edit().putString(ARG_TOKEN, value).apply()
        }

    var uid: String = ""
        get() {
            return field.ifEmpty { sharedPreferences.getString(ARG_UID, "") ?: "" }
        }
        set(value) {
            field = value
            sharedPreferences.edit().putString(ARG_UID, value).apply()
        }

    var username: String = ""
        get() {
            return field.ifEmpty { sharedPreferences.getString(ARG_USERNAME, "") ?: "" }
        }
        set(value) {
            field = value
            sharedPreferences.edit().putString(ARG_USERNAME, value).apply()
        }

    var expiresAt: Long
        get() = sharedPreferences.getLong(ARG_EXPIRES_AT, 0L)
        set(value) = sharedPreferences.edit().putLong(ARG_EXPIRES_AT, value).apply()

    private val _loggedOut = MutableStateFlow(false)
    val loggedOut: StateFlow<Boolean> = _loggedOut

    fun isAccessTokenEmpty(): Boolean = token.isEmpty() || uid.isEmpty()

    fun saveUsername(username: String) {
        this.username = username
    }

    fun updateSession(token: String? = null, id: String? = null) {
        if (token != null && id != null) {
            this.token = token
            this.uid = id
        }
        val timeInSecs = Calendar.getInstance().timeInMillis
        expiresAt = timeInSecs + (3600 * 10000)
    }

    fun clearSession() {
        token = ""
        uid = ""
        username = ""
        expiresAt = 0L
        _loggedOut.value = true
    }

}