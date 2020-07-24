package com.denobili.app.invoice.payment

import android.app.Activity
import android.app.ProgressDialog
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import com.denobili.app.R
import com.denobili.app.helper.DialogUtil
import kotlinx.android.synthetic.main.activity_home.*
import org.jetbrains.anko.imageResource
import java.net.URI
import java.net.URISyntaxException
import java.net.URL


class HomeActivity : AppCompatActivity() {
private var path:String?=null
private var invoiceId:Int?=null
private var amount:String?=null
private var baseUrl:String?=null
    private var progressdialog: ProgressDialog? = null

    /*internal var intent: Intent? = null
    private var extendedButton: Button? = null
    private var simpleButton: Button? = null
*/
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView( R.layout.activity_home)
        progressdialog = DialogUtil.showProgressDialog(this)
        path=intent.getStringExtra("path")
        baseUrl=intent.getStringExtra("baseurl")
        invoiceId=intent.getIntExtra("invoiceId",0)
        amount=intent.getStringExtra("amount")
        progressdialog!!.show()



        webView.webViewClient = MyWebViewClient(this,webView,success,orderId,goback,checkStatus,pay, progressdialog!!, invoiceId!!,amount!!,invoiceId1,amountPay,baseUrl!!)
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.loadUrl(path)
       webView.isClickable=true
        //extendedButton = findViewById<View>(R.id.FL_Extended) as Button
        //simpleButton = findViewById<View>(R.id.FL_Simple) as Button
        //intent = Intent()
    }
    @Throws(URISyntaxException::class)
    private fun getUrlWithoutParameters(url: String): String? {
        val uri = URI(url)
        return URI(uri.getScheme(),
                uri.getAuthority(),
                uri.getPath(),
                null,  // Ignore the query part of the input url
                uri.getFragment()).toString()
    }



    override fun onResume() {
        super.onResume()
        //enableButtons()
    }

   /* private fun enableButtons() {
        extendedButton!!.isEnabled = true
        simpleButton!!.isEnabled = true
        extendedButton!!.text = EXTENDED_BUTTON
        simpleButton!!.text = SIMPLE_BUTTON
    }

    private fun disableButtons() {
        extendedButton!!.isEnabled = false
        simpleButton!!.isEnabled = false
    }*/


  /*  fun extendedButtonClicked(view: View) {
        disableButtons()
        extendedButton!!.text = "Processing..."
        intent!!.setClass(this, FExtendedActivity::class.java)
        startActivity(intent)
    }

    fun directButtonClicked(view: View) {
        disableButtons()
        simpleButton!!.text = "Processing..."
        intent!!.setClass(this, FLSimpleActivity::class.java)
        startActivity(intent)
    }*/
  class MyWebViewClient internal constructor(private val activity: Activity, webView: WebView, success: ImageView, orderId: TextView, goback: TextView, checkStatus: RelativeLayout, pay: TextView, progressdialog: ProgressDialog, invoiceId: Int, amount: String, invoiceId1: TextView, amountPay: TextView, baseUrl: String) : WebViewClient() {
     var web=webView
      var success=success
      var orderId=orderId
      var goback=goback
      var amount=amount
      var amountPay=amountPay
      var invoiceId=invoiceId
      var invoiceId1=invoiceId1
      var progressDialog=progressdialog
      var checkStatus=checkStatus
      var pay=pay
      var baseUrl=baseUrl
      @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
      override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
          val url: String = request?.url.toString();
          view?.loadUrl(url)
          progressDialog.dismiss()
          var sta=getUrlWithoutParameters(url)
          if (sta.equals("https://"+baseUrl)||sta.equals("http://"+baseUrl)) {
               val uri: Uri = Uri.parse(url)
               val server: String = uri.getAuthority()
               val path: String = uri.getPath()
               val protocol: String = uri.getScheme()
               val args: Set<String> = uri.getQueryParameterNames()
               val chapter = uri.getQueryParameter("status_id")
              val order_id = uri.getQueryParameter("order_id")
              orderId.setText("Order Id-"+" "+order_id)
              invoiceId1.setText("Invoice Id-"+" "+invoiceId.toString())
              amountPay.setText("Amount-"+" "+amount.toString())
               if (chapter.equals("21")) {
                   web.visibility = View.GONE
                   checkStatus.visibility = View.VISIBLE
                   goback.setOnClickListener{
                       activity.finish()
                   }

               }else{
                   web.visibility = View.GONE
                   checkStatus.visibility = View.VISIBLE
                   pay.setText("Payment Failed")
                   success.imageResource=R.drawable.ic_error_black_24dp
                   goback.setOnClickListener{
                       activity.finish()
                   }
               }
           }else{}

          return true
      }
      @Throws(URISyntaxException::class)
      private fun getUrlWithoutParameters(url: String): String? {
          val uri = URI(url)
          return URI(uri.getScheme(),
                  uri.getAuthority(),
                  uri.getPath(),
                  null,  // Ignore the query part of the input url
                  uri.getFragment()).toString()
      }

      override fun shouldOverrideUrlLoading(webView: WebView, url: String): Boolean {
          webView.loadUrl(url)
          val aURL = URL(url)
          var conn = aURL.openConnection();
          conn.connect();
          var la = conn.getInputStream();
          return true
      }

      override fun onReceivedError(view: WebView, request: WebResourceRequest, error: WebResourceError) {
          //Toast.makeText(activity, "Got Error! $error", Toast.LENGTH_SHORT).show()
      }
  }
    companion object {
        private val EXTENDED_BUTTON = "FL + JBF Extended"
        private val SIMPLE_BUTTON = "FL + JBF Direct"
    }
}