package com.example.okhttp.auth

import com.example.core.utils.Screen
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.okhttp.MainActivity
import com.example.okhttp.R
import com.example.okhttp.databinding.FragmentSplashBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SplashFragment : Fragment(R.layout.fragment_splash) {

    private var binding: FragmentSplashBinding? = null
    private val viewModel: SplashViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentSplashBinding.bind(view)
        setupObservers()
        viewModel.checkAccessToken()
    }

    private fun setupObservers() {
        viewModel.state.onEach { state ->
            when (state) {
                SplashViewModel.State.GoToAuth -> {
                    goToAuth()
                }

                SplashViewModel.State.GoToMain -> {
                    goToMain()
                }

                is SplashViewModel.State.Error -> {
                    Toast.makeText(context, state.error, Toast.LENGTH_SHORT).show()
                }

                else -> Unit

            }
        }.launchIn(lifecycleScope)

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.effect.collect {
                when (it) {
                    is SplashViewModel.Effect.ShowToast -> {
                        Toast.makeText(
                            context,
                            it.text,
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            }
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