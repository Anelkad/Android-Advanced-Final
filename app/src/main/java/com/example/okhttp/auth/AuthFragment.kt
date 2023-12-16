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
import com.example.okhttp.delegates.DialogDelegate
import com.example.okhttp.delegates.WaitDialogDelegate
import com.example.okhttp.firebase.EventManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AuthFragment : Fragment(R.layout.fragment_auth),
    DialogDelegate by WaitDialogDelegate() {

    private var binding: FragmentAuthBinding? = null
    private val viewModel: AuthViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentAuthBinding.bind(view)
        bindViews()
        setupObservers()
        registerWaitDialogDelegate(this)
    }

    private fun bindViews() {
        binding?.btnLogin?.setOnClickListener {
            login()
        }
    }

    private fun setupObservers() {
        viewModel.state.onEach { state ->
            when (state) {
                is AuthViewModel.State.GoToMain -> {
                    EventManager.logEvent("loginSuccess")
                    goToMain()
                }

                else -> {}
            }
        }.launchIn(lifecycleScope)

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.effect.collect {
                when (it) {
                    is AuthViewModel.Effect.ShowToast -> {
                        Toast.makeText(
                            context,
                            it.text,
                            Toast.LENGTH_LONG
                        ).show()
                    }
                    AuthViewModel.Effect.ShowWaitDialog -> {
                        showWaitDialog()
                    }
                    AuthViewModel.Effect.HideWaitDialog -> {
                        hideWaitDialog()
                    }
                }
            }
        }
    }

    private fun login() {
        val username = binding?.etUsername?.text.toString()
        val password = binding?.etPassword?.text.toString()
        viewModel.login(username, password)
    }

    private fun goToMain() {
        val intent = Intent(context, MainActivity::class.java)
        startActivity(intent)
        activity?.finish()
    }
}