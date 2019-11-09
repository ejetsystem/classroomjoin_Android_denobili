package com.classroomjoin.app.loadWebPage

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.net.http.SslError
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.webkit.*
import com.akexorcist.localizationactivity.ui.LocalizationActivity
import com.classroomjoin.app.R
import kotlinx.android.synthetic.main.activity_take_action.*


class WebPage : LocalizationActivity() {

    private var url: String? = null
    //private String url_is;
    private var handler22: SslErrorHandler? = null

    private var dialog: AlertDialog? = null

    private var progressBar: ProgressDialog? = null


    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_take_action)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_arrow_back_green_24dp)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDefaultDisplayHomeAsUpEnabled(true)
        supportActionBar?.setTitle(getString(R.string.nav_back))

        val bundle = intent.extras
        url = bundle!!.getString("take_url")

        if (progressBar == null) {
            progressBar = ProgressDialog(this@WebPage)
            progressBar!!.setMessage("Please wait...")
            progressBar!!.show()
        }


        /* //val webView = findViewById<View>(R.id.take_webview) as WebView

         take_webview.webViewClient = CustomWebClient()

         take_webview.webChromeClient = MyWebChromeClient()

         take_webview.settings.javaScriptEnabled = true // enable javascript

         take_webview.settings.useWideViewPort = true

         take_webview.settings.loadWithOverviewMode = true

         take_webview.settings.builtInZoomControls = true

         take_webview.loadUrl(url)


         val builder = AlertDialog.Builder(this@WebPage)

         builder.setPositiveButton("Continue") { dialog, which ->
             if (handler22 != null) {
                 handler22!!.proceed()
             }
         }
         builder.setNegativeButton("Cancel") { dialog, which ->
             if (handler22 != null) {

                 handler22!!.cancel()
                 finish()
             }
         }
         dialog = builder.create()*/


        take_webview.settings.javaScriptEnabled = true
        take_webview.settings.loadWithOverviewMode = true
        take_webview.settings.useWideViewPort = true
        take_webview.webViewClient = CustomWebClient()
        take_webview.webChromeClient = MyWebChromeClient()
        take_webview.settings.builtInZoomControls = true



       /* take_webview.setWebViewClient(object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                view?.loadUrl(url);
                return true
            }
        })*/
        take_webview.loadUrl(url)

    }

    private inner class MyWebChromeClient : WebChromeClient() {

        // display alert message in Web View
        override fun onJsAlert(view: WebView, url: String, message: String, result: JsResult): Boolean {
            // Log.d(LOG_TAG, message);
            AlertDialog.Builder(view.context).setMessage(message).setCancelable(true).show()
            result.confirm()
            return true
        }

    }

    /*override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.sign_up_page, menu)
        return true
    }*/

    override fun onPrepareOptionsMenu(menu: Menu): Boolean {
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        if (item!!.itemId == android.R.id.home) {
            finish()
        }
        return true
    }

    inner class CustomWebClient : WebViewClient() {
       // private var progressBar: ProgressDialog? = null

       /* override fun onPageStarted(view: WebView, url: String, favicon: Bitmap) {
            try {
                if (progressBar == null) {
                    progressBar = ProgressDialog(this@WebPage)
                    progressBar!!.setMessage("Please wait...")
                    progressBar!!.show()
                }
            } catch (ignored: Exception) {

            }

            super.onPageStarted(view, url, favicon)

        }*/

        override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
            if (url.startsWith("tel:")) {

                startActivity(Intent.createChooser(Intent(Intent.ACTION_DIAL, Uri.parse(url)),
                        getString(R.string.chooseCallClient)))
                return true

            } else if (url.startsWith("mailto:")) {

                startActivity(Intent.createChooser(Intent(Intent.ACTION_SENDTO, Uri.parse(url)),
                        getString(R.string.chooseEmailClient)))
                return true

            } else {
                view.loadUrl(url)
                return true
            }
        }

        override fun onReceivedSslError(view: WebView, handler: SslErrorHandler, error: SslError) {

            //Log.d("U onReceivedSslError  ", url_is);
            handler22 = handler

            var message = "Certificate error."
            when (error.primaryError) {
                SslError.SSL_UNTRUSTED -> message = "The certificate authority is not trusted."
                SslError.SSL_EXPIRED -> message = "The certificate has expired."
                SslError.SSL_IDMISMATCH -> message = "The certificate Hostname mismatch."
                SslError.SSL_NOTYETVALID -> message = "The certificate is not yet valid."
            }
            message += " Do you want to continue anyway?"

            dialog!!.setMessage(message)
            dialog!!.setTitle("SSL Certificate Error")
            if (dialog != null && dialog!!.isShowing)
                dialog!!.dismiss()
            dialog!!.show()

        }

        override fun onPageFinished(view: WebView, url: String) {
            super.onPageFinished(view, url)
            try {
                if (progressBar != null) {
                    progressBar!!.cancel()
                    progressBar = null
                }
            } catch (ignored: Exception) {

            }

        }
    }


}
