package com.example.okhttp.alert

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import com.example.okhttp.databinding.AlertLimitationBinding

class LimitationAlert(
    context: Context,
    private val onDismissAction: (() -> Unit)? = null
) : Dialog(context) {

    val binding = AlertLimitationBinding.inflate(LayoutInflater.from(context))

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setCancelable(false)
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        binding.btnOk.setOnClickListener {
            onDismissAction?.invoke()
            dismiss()
        }
    }
}