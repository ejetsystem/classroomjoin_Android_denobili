package com.denobili.app.invoice.payment

import `in`.juspay.godel.ui.JuspayBrowserFragment
import `in`.juspay.godel.ui.JuspayWebView
import android.os.Bundle
import com.denobili.app.R

class FLSimpleActivity : BaseActivity() {
    internal var juspayBrowserFragment: JuspayBrowserFragment? = null
    internal var webView: JuspayWebView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_frame_layout)

        if (savedInstanceState != null) {
            juspayBrowserFragment = supportFragmentManager.getFragment(savedInstanceState, "juspayBrowserFragment") as JuspayBrowserFragment
        } else {
            juspayBrowserFragment = JuspayBrowserFragment()
            setJuspayArgumentBundle(juspayBrowserFragment!!)
            val transaction = supportFragmentManager.beginTransaction()
            transaction.add(R.id.container, juspayBrowserFragment)
            transaction.commit()
        }
        setupCallbacks(juspayBrowserFragment!!)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        // Put the lastFragment in the outState Bundle
        supportFragmentManager.putFragment(outState, "juspayBrowserFragment", juspayBrowserFragment)
        super.onSaveInstanceState(outState)
    }


    override fun onBackPressed() {
        if (juspayBrowserFragment != null) {
            juspayBrowserFragment!!.juspayBackPressedHandler(true)
        } else {
            super.onBackPressed()
        }
    }
}