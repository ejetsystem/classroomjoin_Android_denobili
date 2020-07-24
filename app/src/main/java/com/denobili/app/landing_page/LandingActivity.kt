package com.denobili.app.landing_page

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.denobili.app.R
import kotlinx.android.synthetic.main.activity_landing.*


class LandingActivity : AppCompatActivity(),View.OnClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_landing)
        button_sign_submit.setOnClickListener(this)
        signin.setOnClickListener(this)
    }
    override fun onClick(v: View?) {
        when(v?.getId()) {
            R.id.button_sign_submit ->
                showErrorDialog(getString(R.string.denobili_aert))
               // SignALert()
            R.id.signin -> loginAlert()
        }
    }
    private fun loginAlert() {
        intent = Intent(this, AccountOption::class.java)
        intent.putExtra("firstEntry","1")
        startActivity(intent)
        overridePendingTransition( R.anim.slide_in_up, R.anim.stay );
    }
    private fun SignALert() {
        intent = Intent(this, SignUp::class.java)
        intent.putExtra("firstEntry","1")
        startActivity(intent)
        overridePendingTransition( R.anim.slide_in_up, R.anim.stay );
    }
    private fun showErrorDialog(message: String?) {
        var alert = android.app.AlertDialog.Builder(this)
        alert.setTitle(R.string.app_name)
        alert.setMessage(message)

        alert.setPositiveButton(R.string.btn_ok) { dialog, whichButton ->
            dialog.dismiss()
            // finish()
        }
        alert.show()
    }
}
