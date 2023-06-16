package com.capstone.karira.activity.payment

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.widget.AppCompatButton
import com.capstone.karira.R
import com.capstone.karira.activity.MainActivity

class PaymentActivity : AppCompatActivity() {

    private lateinit var btnCloseSnap: AppCompatButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment)

        btnCloseSnap = findViewById(R.id.btn_closeSnap)
        btnCloseSnap.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        val url = intent.getStringExtra("URL")
        openUrlFromWebView(url)
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun openUrlFromWebView(url: String?) {
        val webView: WebView = findViewById(R.id.myWebView)
        webView.webViewClient = object : WebViewClient() {
            private val pd: ProgressDialog = ProgressDialog(this@PaymentActivity)

            override fun shouldOverrideUrlLoading(view: WebView, request: WebResourceRequest): Boolean {
                val requestUrl = request.url.toString()
                if (requestUrl.contains("gojek://")
                    || requestUrl.contains("shopeeid://")
                    || requestUrl.contains("//wsa.wallet.airpay.co.id/")
                    || requestUrl.contains("/gopay/")
                    || requestUrl.contains("/shopeepay/")
                ) {
                    val intent = Intent(Intent.ACTION_VIEW, request.url)
                    startActivity(intent)
                    return true
                } else {
                    return false
                }
            }

            override fun onPageStarted(view: WebView, url: String, favicon: Bitmap?) {
                pd.setMessage("loading")
                pd.show()
                super.onPageStarted(view, url, favicon)
            }

            override fun onPageFinished(view: WebView, url: String) {
                pd.hide()
                super.onPageFinished(view, url)
            }
        }

        webView.settings.loadsImagesAutomatically = true
        webView.settings.javaScriptEnabled = true
        webView.scrollBarStyle = View.SCROLLBARS_INSIDE_OVERLAY
        if (url != null) {
            webView.loadUrl(url)
        }
    }

}