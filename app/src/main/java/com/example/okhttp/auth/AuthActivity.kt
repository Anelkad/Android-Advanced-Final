package com.example.okhttp.auth

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.core.utils.Screen
import com.example.okhttp.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AuthActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)
        initScreen()
    }

    private fun initScreen() {
        val screen = intent.getStringExtra(Screen.SCREEN)
        val fragment = when (screen) {
            Screen.AUTH -> AuthFragment()
            Screen.SPLASH -> SplashFragment()
            else -> SplashFragment()
        }
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, fragment)
            .commit()
    }
}