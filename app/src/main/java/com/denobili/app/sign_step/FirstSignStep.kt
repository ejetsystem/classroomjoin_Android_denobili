package com.denobili.app.sign_step

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.support.annotation.RequiresApi
import android.support.v4.app.Fragment
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.text.method.LinkMovementMethod
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.navigation.findNavController
import com.denobili.app.R
import com.denobili.app.helper.DialogUtil
import com.denobili.app.helper.EditTextUtil
import com.denobili.app.helper_utils.Event
import com.denobili.app.helper_utils.MainBus
import com.denobili.app.loadWebPage.WebPage
import com.denobili.app.signUpPage.getOTPsignup.SignupOTPEvent
import com.denobili.app.signUpPage.getOTPsignup.SignupOTPModel
import com.denobili.app.signUpPage.getOTPsignup.SignupOTPPresenter
import com.denobili.app.sign_step.signup_manager.PinEntryEditText
import com.denobili.app.utils.SharedPreferencesData
import com.thekhaeng.pushdownanim.PushDownAnim
import kotlinx.android.synthetic.main.final_sign.*
import kotlinx.android.synthetic.main.final_sign.terms_of_service
import kotlinx.android.synthetic.main.first_step_sign.*
import kotlinx.android.synthetic.main.second_sign_step.*
import kotlinx.android.synthetic.main.sign_up.*
import lt.neworld.spanner.Spanner
import lt.neworld.spanner.Spans
import org.jetbrains.anko.bundleOf
import rx.Observer

class FirstSignStep : Fragment(), View.OnClickListener{
    companion object FirstSignStep {
        var IS_GOOGLE: String = "com.classroom.is_google_login"
        var IS_EMAIL: String = "com.classroom.is_google_email"

    }
    private var signup_otp_presenter: SignupOTPPresenter? = null
    var sharedPreferencesData: SharedPreferencesData? = null
    var mobileNum:String?=null
    var resend:String?=""
    private var progressdialog: ProgressDialog? = null
    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.first_step_sign, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        pass_lay.visibility=View.GONE
        pass_view1.visibility=View.GONE
        val spannable = Spanner().append(getString(R.string.by_continuing))
                .append(getString(R.string.terms) + " ", Spans.click(onClickListener))
                .append(getString(R.string.and_add) + " ")
                .append(getString(R.string.privacy_policy), Spans.click(onClickListener11))
        terms_of_service.setMovementMethod(LinkMovementMethod());

        terms_of_service.text = spannable
        my_text_view.setMovementMethod(LinkMovementMethod());

        my_text_view.text = spannable
        signup_otp_presenter = SignupOTPPresenter(activity!!)
        sharedPreferencesData = SharedPreferencesData(activity)
        MainBus.getInstance().busObservable.ofType(SignupOTPEvent::class.java).subscribe(otpeventobserver)
        progressdialog = DialogUtil.showProgressDialog(activity!!)
        signTxt.setText("Sign Up")
        goBack.setOnClickListener(this)
        email_tap.setOnClickListener(this)
        phone_tab.setOnClickListener(this)
        nextToC.setOnClickListener(this)
        user_email.addTextChangedListener(textWatcher)
        number.addTextChangedListener(textWatcher)

        PushDownAnim.setPushDownAnimTo(emailLog).setScale(PushDownAnim.MODE_STATIC_DP,5F).setOnClickListener {
            if (user_email.isInvalid(getString(R.string.email_prompt)))
          else  if (!isValidEmail(user_email.text))
                else if (!EditTextUtil.isValidEmail(user_email.text.toString())) {
                user_email.error = getString(R.string.email_prompt)
            }
                else{
                var bundle= bundleOf("email" to user_email.text.toString().trim())
                view.findNavController().navigate(R.id.fragmentCtoD,bundle)
            }

        }

    }
    fun EditText.isInvalid(error: String): Boolean {
        if (this.text.isNullOrBlank()) {
            this.error = error
            return true
        } else return false
    }
    fun isValidEmail(target: CharSequence): Boolean {
        return !TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches()
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
        if (number.text.toString().length!=0){
            nextToC.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#009688"))
            nextToC.setTextColor(Color.WHITE);}
        else
        {
            nextToC.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#ECECEC"))
            nextToC.setTextColor(Color.GRAY);
        }

        if (user_email.text.toString().length!=0){
            emailLog.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#009688"))
            emailLog.setTextColor(Color.WHITE);
        }
        else{
            emailLog.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#ECECEC"))
            emailLog.setTextColor(Color.GRAY);
        }

    }

    override fun onClick(v: View?) {
        when(v?.getId()) {
            R.id.email_tap ->
                emailOption()
            R.id.phone_tab->
                phoneOption()
            R.id.goBack->
                activity?.finish()
            R.id.nextToC->
                if (number.isInvalid(getString(R.string.input_mobile_number))){}
                 else if (number.text.toString().length<=9){}
                 else  {
                    mobileNum=number.text.toString().trim()
                    progressdialog?.show()
                    if ( IS_GOOGLE.equals("true")) {

                        if (number.text.trim().length > 0 && sign_up_email_input.text.trim().length > 0) {
                            if (isValidEmail(sign_up_email_input.text)) {

                                if (number.text.trim().length == 10)
                                    signup_otp_presenter!!.postdata(getSignUpModelOTPPhone())
                                else
                                    showerrorDialog1("Please Enter 10 digit Mobile No.")

                            } else {
                                Toast.makeText(context, getString(R.string.enter_validEmail), Toast.LENGTH_LONG).show()
                            }
                        }
                    }
                    else{
                        verifyOtp()
                    }

                }

        }
    }

    private fun showerrorDialog1(message: String?) {

        if (progressdialog!!.isShowing)
            progressdialog!!.dismiss()

        var alert = android.app.AlertDialog.Builder(activity)
        alert.setTitle(R.string.app_name)
        alert.setMessage(message)

        alert.setPositiveButton(R.string.btn_ok) { dialog, whichButton ->
            dialog.dismiss()
            // finish()
        }
        alert.show()
    }
    private fun verifyOtp() {
        signup_otp_presenter!!.postdata(getSignUpModelOTPPhone())
    }
    private fun getSignUpModelOTPPhone(): SignupOTPModel {
        return SignupOTPModel("",
                number.text.toString())
    }
    private val otpeventobserver = object : Observer<SignupOTPEvent> {
        override fun onCompleted() = Unit

        override fun onError(e: Throwable) = e.printStackTrace()

        override fun onNext(event: SignupOTPEvent) {
            if (progressdialog!!.isShowing) progressdialog!!.dismiss()
            when (event.event) {
                Event.POST_SUCCESS -> {
                  if (resend.equals(""))
                    showSuccess()
                    else
                  {
                      showSuccessDialog1(getString(R.string.success_forgot11))
                  }
                    //showSuccessDialog(getString(R.string.success_forgot11), 1)


                }
                Event.POST_FAILURE -> showerrorDialog(event.error)

                Event.ERROR -> showSuccessDialog1(event.error)

                else -> {
                }
            }
        }
    }

    private fun showSuccess() {
        if (progressdialog!!.isShowing)
            progressdialog!!.dismiss()
        val dialogBuilder = AlertDialog.Builder(activity)
        val inflater = this.getLayoutInflater()
        @SuppressLint("InflateParams")
        val dialogView = inflater.inflate(R.layout.otp_page, null)
        dialogBuilder.setView(dialogView)
        val alertDialog = dialogBuilder.create()
        var subButton=dialogView.findViewById<Button>(R.id.submit)
        var timer=dialogView.findViewById<TextView>(R.id.timer)
        startTime(timer)
        var close=dialogView.findViewById<ImageView>(R.id.back)
        var pinCode=dialogView.findViewById<PinEntryEditText>(R.id.txt_pin_entry)
       var resendOtp=dialogView.findViewById<TextView>(R.id.resendOtp)
        resendOtp.setOnClickListener{
            pinCode.setText("")
            resend="resendOtp"
            startTime(timer)
            signup_otp_presenter!!.postdata(getSignUpModelOTPPhone())
        }

        close.setOnClickListener{alertDialog.dismiss()}
        subButton.setOnClickListener{
            if (timer.text.toString().equals("Try Again")){
                showerrorDialog(getString(R.string.expired))
            }else{
                if (pinCode.text.toString().length==0){
                    showerrorDialog(getString(R.string.please_enter_otp))
                }
                else   if (pinCode.text.toString().equals(sharedPreferencesData?.signup_otp)&& sharedPreferencesData?.social_email!=""){
                    alertDialog.dismiss()
                    var bundle= bundleOf("mobile" to mobileNum.toString(),"is_google" to IS_GOOGLE)
                    view?.findNavController()?.navigate(R.id.action_fragmentA_to_fragmente,bundle)
                }
                else if (pinCode.text.toString().equals(sharedPreferencesData?.signup_otp)&& sharedPreferencesData?.social_email==""){
                    alertDialog.dismiss()
                    var bundle= bundleOf("mobile" to mobileNum.toString(),"is_google" to IS_GOOGLE)
                    view?.findNavController()?.navigate(R.id.fragmentCtoD,bundle)
                }
                else{
                    showerrorDialog(getString(R.string.enter_valid_otp))
                }
            }


        }
        alertDialog.show()
    }

    private fun startTime(timer: TextView?) {
        object : CountDownTimer(30000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                timer!!.text = "00 : "+millisUntilFinished/1000
            }
            override fun onFinish() {
                timer!!.text="Try Again"
            }
        }.start()
    }

    private fun showerrorDialog(message: String?){
        if (progressdialog!!.isShowing)
            progressdialog!!.dismiss()
        var alert = AlertDialog.Builder(activity)
        alert.setTitle(R.string.error)
        alert.setMessage(message)
        alert.setPositiveButton(R.string.btn_ok) { dialog, whichButton ->

            dialog.dismiss()
            // finish()
        }
        alert.show()
    }
    private fun showSuccessDialog1(message: String?) {

        if (progressdialog!!.isShowing)
            progressdialog!!.dismiss()

        var alert = AlertDialog.Builder(activity)
        alert.setTitle(R.string.success)
        alert.setMessage(message)
        alert.setPositiveButton(R.string.btn_ok) { dialog, whichButton ->

            dialog.dismiss()
            PushDownAnim.setPushDownAnimTo(emailLog).setScale(PushDownAnim.MODE_STATIC_DP,5F).setOnClickListener {
                var bundle= bundleOf("mobile" to user_email.text.toString().trim(),"otp" to message.toString())

                view?.findNavController()?.navigate(R.id.fragmentCtoD,bundle)
            }
            // finish()
        }
        alert.show()
    }
    private fun phoneOption() {
        phone.visibility=View.VISIBLE
        emailoption.visibility=View.GONE
        phone_tab.setBackgroundResource(R.drawable.bottom_rectangle)
        email_tap.setBackgroundResource(0)    }

    private fun emailOption() {
        phone.visibility=View.GONE
        emailoption.visibility=View.VISIBLE
        email_tap.setBackgroundResource(R.drawable.bottom_rectangle)
        phone_tab.setBackgroundResource(0)    }
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