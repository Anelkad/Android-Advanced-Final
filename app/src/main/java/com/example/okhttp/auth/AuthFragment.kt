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
import com.example.okhttp.databinding.FragmentAuthBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class AuthFragment : Fragment(R.layout.fragment_auth) {

    private var binding: FragmentAuthBinding? = null
    private val viewModel: AuthViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentAuthBinding.bind(view)
        bindViews()
        setupObservers()
    }

    private fun bindViews() {
        binding?.btnLogin?.setOnClickListener {
            login()
        }
    }

    private fun setupObservers() {
        viewModel.state.onEach { state ->
            when (state) {
                is AuthViewModel.State.HideLoading -> {}

                is AuthViewModel.State.ShowLoading -> {}

                is AuthViewModel.State.Error -> {
                        Toast.makeText(context, state.error, Toast.LENGTH_SHORT).show()
                }

                is AuthViewModel.State.LoggedIn -> {
                    if (state.loggedIn) {
                        goToMain()
                    }
                }

                else -> {}
            }
        }.launchIn(lifecycleScope)
    }

    private fun login() {
        val username = binding?.etUsername?.text.toString()
        val password = binding?.etPassword?.text.toString()
        if (password.isNotEmpty() && username.isNotEmpty()) {
            viewModel.login(username, password)
        }
    }

    private fun goToMain() {
        val intent = Intent(context, MainActivity::class.java)
        startActivity(intent)
        activity?.finish()
    }

}