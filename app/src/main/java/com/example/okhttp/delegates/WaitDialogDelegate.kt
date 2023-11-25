package com.example.okhttp.delegates

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.fragment.app.Fragment
import com.example.okhttp.R

interface DialogDelegate {
    fun registerWaitDialogDelegate(fragment: Fragment)
    fun showWaitDialog()
    fun hideWaitDialog()
}

class WaitDialogDelegate : DialogDelegate {
    private var fragment: Fragment? = null
    private var waitDialog: Dialog? = null

    override fun registerWaitDialogDelegate(fragment: Fragment) {
        this.fragment = fragment
    }

    override fun showWaitDialog() {
        if (waitDialog == null) {
            waitDialog = fragment?.requireContext()?.let {
                Dialog(it).apply {
                    setContentView(R.layout.wait_dialog)
                    window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                    setCancelable(false)
                    setCanceledOnTouchOutside(false)
                }
            }
        }
        if (waitDialog?.isShowing == false) waitDialog?.show()
    }

    override fun hideWaitDialog() {
        if (waitDialog != null || waitDialog?.isShowing == true) waitDialog?.dismiss()
    }
}