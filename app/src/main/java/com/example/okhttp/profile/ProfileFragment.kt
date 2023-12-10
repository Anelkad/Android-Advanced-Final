package com.example.okhttp.profile

import Screen
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.okhttp.R
import com.example.okhttp.auth.AuthActivity
import com.example.okhttp.data.local.SessionManager
import com.example.okhttp.databinding.FragmentProfileBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@AndroidEntryPoint
class ProfileFragment : Fragment(R.layout.fragment_profile) {
    @Inject
    lateinit var sessionManager: SessionManager

    private var binding: FragmentProfileBinding? = null
    private val viewModel: ProfileViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentProfileBinding.bind(view)
        bindViews()
        setupObservers()
    }

    private fun bindViews() {
        binding?.apply {
            tvUsername.text = sessionManager.username
            tvId.text = sessionManager.uid
            tvSession.text = sessionManager.session
        }
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
                is ProfileViewModel.State.HideLoading -> {}

                is ProfileViewModel.State.ShowLoading -> {}

                is ProfileViewModel.State.Error -> {
                        Toast.makeText(context, state.error, Toast.LENGTH_SHORT).show()
                }

                is ProfileViewModel.State.LoggedIn -> {}

                is ProfileViewModel.State.LoggedOut -> {
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