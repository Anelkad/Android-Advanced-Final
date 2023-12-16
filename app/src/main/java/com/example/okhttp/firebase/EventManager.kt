package com.example.okhttp.firebase

import android.os.Bundle
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase

object EventManager {

    fun logEvent(eventName: String, bundle: Bundle? = null) {
        Firebase.analytics.logEvent(eventName, bundle)
    }

}