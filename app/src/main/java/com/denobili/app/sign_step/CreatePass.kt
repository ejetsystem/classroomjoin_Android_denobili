package com.denobili.app.sign_step

import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.v4.app.Fragment
import android.text.Editable
import android.text.TextWatcher
import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.navigation.findNavController
import com.denobili.app.R
import com.denobili.app.loadWebPage.WebPage
import com.thekhaeng.pushdownanim.PushDownAnim
import kotlinx.android.synthetic.main.create_pass.*
import kotlinx.android.synthetic.main.create_pass.close
import kotlinx.android.synthetic.main.create_pass.nextToD
import kotlinx.android.synthetic.main.create_pass.terms_of_service
import lt.neworld.spanner.Spanner
import lt.neworld.spanner.Spans
import org.jetbrains.anko.bundleOf

class CreatePass : Fragment() {
    var userEmail:String?=null
    var mobileNum:String?=null
    var otpMob:String?=null
    var is_google:String?=null
    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.create_pass, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val spannable = Spanner().append(getString(R.string.by_continuing))
                .append(getString(R.string.terms) + " ", Spans.click(onClickListener))
                .append(getString(R.string.and_add) + " ")
                .append(getString(R.string.privacy_policy), Spans.click(onClickListener11))
        terms_of_service.setMovementMethod(LinkMovementMethod());

        terms_of_service.text = spannable

        password.addTextChangedListener(textWatcher)
        userEmail=arguments?.getString("email")
        mobileNum=arguments?.getString("mobile")
        otpMob=arguments?.getString("otp")
        is_google=arguments?.getString("is_google")
        PushDownAnim.setPushDownAnimTo(nextToD).setScale(PushDownAnim.MODE_STATIC_DP,5F).setOnClickListener {
            if (password.isInvalid(getString(R.string.pls_pass)))
                else if (mobileNum!=null){
                var bundle= bundleOf("mobile" to mobileNum.toString(),"password" to password.text.toString().trim(),"otp" to otpMob.toString(),"is_google" to is_google.toString())
                view.findNavController().navigate(R.id.action_fragmentd_to_fragmente,bundle)
            }else{
                var bundle= bundleOf("email" to userEmail.toString(),"password" to password.text.toString().trim())
                view.findNavController().navigate(R.id.action_fragmentd_to_fragmente,bundle)
            }

           // activity?.findViewById<Stepper>(R.id.Stepper)?.forward()
        }
        PushDownAnim.setPushDownAnimTo(close).setScale(PushDownAnim.MODE_STATIC_DP,5F).setOnClickListener {
            view.findNavController().popBackStack()
            // activity?.findViewById<Stepper>(R.id.Stepper)?.forward()
        }

    }
    val textWatcher = object : TextWatcher {
        override
        fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2:Int) {

        }

        override
        fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {

        }

        @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
        override
        fun afterTextChanged(editable: Editable?) {
            checkFieldsForEmptyValues()
        }
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun checkFieldsForEmptyValues() {
        if (password.text.toString().length!=0){
            nextToD.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#009688"))
            nextToD.setTextColor(Color.WHITE);}
        else
        {
            nextToD.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#ECECEC"))
            nextToD.setTextColor(Color.GRAY);
        }
    }

    fun EditText.isInvalid(error: String): Boolean {
        if (this.text.isNullOrBlank()) {
            this.error = error
            return true
        } else return false
    }
    var onClickListener: View.OnClickListener = View.OnClickListener { view ->
        // Toast.makeText(view.context, "Terms of service Clicked", Toast.LENGTH_LONG).show()

        var b = Bundle()
        b.putString("take_url", "https://www.google.co.in/")
        val intent = Intent(activity, WebPage::class.java)
        intent.putExtras(b)
        startActivity(intent)
    }

    var onClickListener11: View.OnClickListener = View.OnClickListener { view ->
        // Toast.makeText(view.context, "Privacy Policy Clicked", Toast.LENGTH_LONG).show()
        var b = Bundle()
        b.putString("take_url", "https://play.google.com/store")
        val intent = Intent(activity, WebPage::class.java)
        intent.putExtras(b)
        startActivity(intent)

    }
}