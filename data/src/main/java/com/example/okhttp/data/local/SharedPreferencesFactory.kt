package com.example.okhttp.data.local

import com.example.core.utils.SharedPrefsConstants.ENCRYPTED_SHARED_PREFERENCES
import com.example.core.utils.SharedPrefsConstants.REGULAR_SHARED_PREFERENCES
import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey

object SharedPreferencesFactory {

    enum class Type {
        REGULAR, ENCRYPTED
    }

    fun getSharedPreferences(type: Type, context: Context): SharedPreferences {
        return when (type) {
            Type.REGULAR -> {
                context.getSharedPreferences(REGULAR_SHARED_PREFERENCES, Context.MODE_PRIVATE)
            }
            Type.ENCRYPTED -> {
                EncryptedSharedPreferences.create(
                    context,
                    ENCRYPTED_SHARED_PREFERENCES,
                    MasterKey.Builder(context).setKeyScheme(MasterKey.KeyScheme.AES256_GCM).build(),
                    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
                )
            }
        }
    }

}