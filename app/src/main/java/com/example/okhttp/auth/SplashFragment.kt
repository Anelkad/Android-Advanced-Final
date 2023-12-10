package com.example.okhttp.auth

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.okhttp.MainActivity
import com.example.okhttp.R
import com.example.okhttp.data.local.SessionManager
import com.example.okhttp.databinding.FragmentSplashBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@AndroidEntryPoint
class SplashFragment : Fragment(R.layout.fragment_splash) {

    private var binding: FragmentSplashBinding? = null

    @Inject
    lateinit var sessionManager: SessionManager
    private val viewModel: SplashViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentSplashBinding.bind(view)
        setupObservers()
        if (sessionManager.isAccessTokenEmpty()) viewModel.getNewToken()
        else goToMain()
    }

    private fun setupObservers() {

        viewModel.state.onEach { state ->
            when (state) {
                is SplashViewModel.State.HideLoading -> {}

                is SplashViewModel.State.ShowLoading -> {}

                is SplashViewModel.State.Error -> {
                    Toast.makeText(context, state.error, Toast.LENGTH_SHORT).show()
                }

                is SplashViewModel.State.LoggedIn -> {
                    if (state.loggedIn) {
                        goToMain()
                    }
                }

                is SplashViewModel.State.NewToken -> {
                    navigateNext()
                }

                else -> Unit

            }
        }.launchIn(lifecycleScope)
    }

    private fun navigateNext() {
        if (sessionManager.isAccessSessionEmpty()) {
            goToAuth()
        } else {
            goToMain()
        }
    }

    private fun goToMain() {
        val intent = Intent(context, MainActivity::class.java)
        startActivity(intent)
        activity?.finish()
    }

    private fun goToAuth() {
        val intent = Intent(context, AuthActivity::class.java)
        intent.putExtra(Screen.SCREEN, Screen.AUTH)
        startActivity(intent)
        activity?.finish()
    }

}