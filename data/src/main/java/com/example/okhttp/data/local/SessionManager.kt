package com.example.okhttp.data.local

import ARG_SESSION
import ARG_TOKEN
import ARG_UID
import ARG_USERNAME
import ENCRYPTED_SHARED_PREFERENCES
import SESSION_ID
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

    var session: String = "" //SESSION_ID
        get() {
            return field.ifEmpty { sharedPreferences.getString(ARG_SESSION, "") ?: "" }
        }
        set(value) {
            field = value
            sharedPreferences.edit().putString(ARG_SESSION, value).apply()
        }

    var username: String = ""
        get() {
            return field.ifEmpty { sharedPreferences.getString(ARG_USERNAME, "") ?: "" }
        }
        set(value) {
            field = value
            sharedPreferences.edit().putString(ARG_USERNAME, value).apply()
        }

    private val _loggedOut = MutableStateFlow(false)
    val loggedOut: StateFlow<Boolean> = _loggedOut

    fun isAccessSessionEmpty(): Boolean = session.isEmpty()
    fun isAccessTokenEmpty(): Boolean = token.isEmpty()
    fun saveUser(username: String?, id: String?) {
        if (username.isNullOrEmpty() || id.isNullOrEmpty()) return
        this.username = username
    }

    fun saveToken(token: String?) {
        if (token.isNullOrEmpty()) return
        this.token = token
    }

    fun saveSession(session: String?) {
        if (session.isNullOrEmpty()) return
        this.session = session
    }

    fun clearSession() {
        token = ""
        uid = ""
        username = ""
        _loggedOut.value = true
    }
}