package com.denobili.app.slide_login

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import com.denobili.app.R
import com.thekhaeng.pushdownanim.PushDownAnim
import kotlinx.android.synthetic.main.login_option.*

class LoginOption : Fragment() ,View.OnClickListener{
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?): View? {
    return inflater.inflate(R.layout.login_option, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    loginEmail.setOnClickListener(this)
    PushDownAnim.setPushDownAnimTo(nextToD).setScale(PushDownAnim.MODE_STATIC_DP,5F).setOnClickListener {
    view.findNavController().popBackStack()
    }

    }

    override fun onClick(v: View?) {
        when(v?.getId()) {
            R.id.loginEmail ->
                signEmail()
            //R.id.signin -> loginAlert()
        }
    }

    private fun signEmail() {
        val intent = Intent(activity, LoginMainActivty::class.java)
        startActivity(intent)
    }
}