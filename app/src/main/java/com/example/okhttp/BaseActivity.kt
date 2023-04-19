package com.example.okhttp

import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import okhttp3.OkHttpClient
import okhttp3.Request
import java.net.URL

open class BaseActivity : AppCompatActivity() {

    private lateinit var waitDialog: Dialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_base)
    }

    fun getRequest(sUrl: String): String? {

        // Create OkHttp Client
        var client: OkHttpClient = OkHttpClient();

        var result: String? = null
        try {
            // Create URL
            val url = URL(sUrl)
            // Build request
            val request = Request.Builder().url(url).build()
            // Execute request
            val response = client.newCall(request).execute()
            result = response.body?.string()
            Log.d("qwerty: ", result!!)
        }
        catch(err:Error) {
            print("Error when executing get request: "+err.localizedMessage)
        }
        return result
    }

    fun showWaitDialog(){
        waitDialog = Dialog(this)
        waitDialog.setContentView(R.layout.wait_dialog)

        waitDialog.setCancelable(false)
        waitDialog.setCanceledOnTouchOutside(false)

        waitDialog.show()
    }

    fun hideWaitDialog(){
        waitDialog.dismiss()
    }
}