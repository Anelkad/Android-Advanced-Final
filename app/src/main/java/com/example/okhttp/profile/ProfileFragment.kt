package com.example.okhttp.profile

import com.example.core.utils.Screen
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.okhttp.R
import com.example.okhttp.auth.AuthActivity
import com.example.okhttp.databinding.FragmentProfileBinding
import com.example.okhttp.firebase.EventManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class ProfileFragment : Fragment(R.layout.fragment_profile) {
    private var binding: FragmentProfileBinding? = null
    private val viewModel: ProfileViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentProfileBinding.bind(view)
        bindViews()
        setupObservers()
        viewModel.getProfile()
    }

    private fun bindViews() {
        binding?.btnLogout?.setOnClickListener {
            logout()
        }
    }

    private fun logout() {
        viewModel.logout()
    }

    private fun setupObservers() {
        viewModel.state.onEach { state ->
            when (state) {
                is ProfileViewModel.State.Profile -> {
                    binding?.apply {
                        tvUsername.text = state.username
                        tvId.text = state.id
                        tvSession.text = state.session
                    }
                }

                is ProfileViewModel.State.LoggedOut -> {
                    EventManager.logEvent("logoutSuccess")
                    goToAuth()
                }

                else -> {}
            }
        }.launchIn(lifecycleScope)
    }

    private fun goToAuth() {
        val intent = Intent(context, AuthActivity::class.java)
        intent.putExtra(Screen.SCREEN, Screen.AUTH)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }
}