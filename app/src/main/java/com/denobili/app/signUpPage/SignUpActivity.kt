package com.denobili.app.signUpPage

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.text.method.LinkMovementMethod
import android.util.Patterns
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.Toast
import com.denobili.app.R
import com.denobili.app.helper.DialogUtil
import com.denobili.app.helper.EditTextUtil
import com.denobili.app.helper.NetworkHelper
import com.denobili.app.helper_utils.Event
import com.denobili.app.helper_utils.MainBus
import com.denobili.app.loadWebPage.WebPage
import com.denobili.app.otpReaderLibrary.OTPListenerSignup
import com.denobili.app.signUpPage.getOTPsignup.SignupOTPEvent
import com.denobili.app.signUpPage.getOTPsignup.SignupOTPModel
import com.denobili.app.signUpPage.getOTPsignup.SignupOTPPresenter
import com.denobili.app.utils.SharedPreferencesData
import kotlinx.android.synthetic.main.sign_up.*
import lt.neworld.spanner.Spanner
import lt.neworld.spanner.Spans.click
import rx.Observer
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Pattern


class SignUpActivity : AppCompatActivity(), OTPListenerSignup {

    private var progressdialog: ProgressDialog? = null
    private var presenter: SignUpPresenter? = null
    private var signup_otp_presenter: SignupOTPPresenter? = null
    private var context: Context? = null
    var sharedPreferencesData: SharedPreferencesData? = null
    private var IS_GOOGLE: String = "false"
    private var GOOGLE_PERSION_ID: String? = null
    private var USERTYPE_ID: String? = null

    private var EMAIL_HAVE: String? = null


    companion object SignUpActivity {
        var IS_GOOGLE: String = "com.classroom.is_google_login"
        var IS_EMAIL: String = "com.classroom.is_google_email"

    }


    @SuppressLint("RestrictedApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.sign_up)


        progressdialog = DialogUtil.showProgressDialog(this@SignUpActivity)

        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_arrow_back_green_24dp)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDefaultDisplayHomeAsUpEnabled(true)
        supportActionBar?.setTitle(getString(R.string.btn_signup))

        usertype_parent.isChecked = true

        MainBus.getInstance().busObservable.ofType(SignUpEvent::class.java).subscribe(eventobserver)
        MainBus.getInstance().busObservable.ofType(SignupOTPEvent::class.java).subscribe(otpeventobserver)

        presenter = SignUpPresenter(this)
        signup_otp_presenter = SignupOTPPresenter(this)


        val spannable = Spanner().append(getString(R.string.by_clicking))
                .append(getString(R.string.terms) + " ", click(onClickListener))
                .append(getString(R.string.and) + " ")
                .append(getString(R.string.privacy_policy), click(onClickListener11))

        terms_of_service.setMovementMethod(LinkMovementMethod());

        terms_of_service.text = spannable

        signup_button.setOnClickListener(onClickListener_button)

        sign_up_email_input.setOnFocusChangeListener { v, hasFocus ->
            if (!hasFocus)
                println("Data11--->else" + "Email loss focus")
        }
        sign_up_mobile_input.setOnFocusChangeListener { v, hasFocus ->
            if (!hasFocus)
                println("Data11--->else" + "Mobile number loss focus")
        }

        sign_up_password_input.setOnFocusChangeListener { v, hasFocus ->

            if (hasFocus) {

                if (sign_up_mobile_input.text.trim().length > 0 && sign_up_email_input.text.trim().length > 0) {

                    if (isValidEmail(sign_up_email_input.text)) {

                        if (sign_up_mobile_input.text.trim().length == 10)
                            signup_otp_presenter!!.postdata(getSignUpModelOTPPhone())
                        else
                            showErrorDialog("Please Enter 10 digit Mobile No.")
                    } else {
                        Toast.makeText(context, getString(R.string.enter_validEmail), Toast.LENGTH_LONG).show()
                    }

                } else if (sign_up_email_input.text.trim().length > 0)
                    signup_otp_presenter!!.postdata(getSignUpOTPModel())
            }
        }

        sign_up_password_input_otp.setOnFocusChangeListener { v, hasFocus ->

            if (hasFocus && IS_GOOGLE.equals("true")) {

                if (sign_up_mobile_input.text.trim().length > 0 && sign_up_email_input.text.trim().length > 0) {
                    if (isValidEmail(sign_up_email_input.text)) {

                        if (sign_up_mobile_input.text.trim().length == 10)
                            signup_otp_presenter!!.postdata(getSignUpModelOTPPhone())
                        else
                            showErrorDialog("Please Enter 10 digit Mobile No.")

                    } else {
                        Toast.makeText(context, getString(R.string.enter_validEmail), Toast.LENGTH_LONG).show()
                    }
                } else if (sign_up_email_input.text.trim().length > 0)
                    signup_otp_presenter!!.postdata(getSignUpOTPModel())
            }
        }


    }

    private fun showErrorDialog(message: String?) {

        if (progressdialog!!.isShowing)
            progressdialog!!.dismiss()

        var alert = android.app.AlertDialog.Builder(this)
        alert.setTitle(R.string.app_name)
        alert.setMessage(message)

        alert.setPositiveButton(R.string.btn_ok) { dialog, whichButton ->
            dialog.dismiss()
            // finish()
        }
        alert.show()
    }


    private fun parseCode(message: String): String {
        val p = Pattern.compile("\\b\\d{4}\\b")
        val m = p.matcher(message)
        var code = ""
        while (m.find()) {
            code = m.group(0)
        }
        return code
    }


    override fun otpReceived(smsText: String) {
        //Do whatever you want to do with the text
        //Toast.makeText(this, "Got " + smsText, Toast.LENGTH_LONG).show()
        // Log.d("Otp", smsText)
        // println("Data11--->" + smsText)
        println("Data22--->" + parseCode(smsText))

        if (parseCode(smsText).isNotEmpty() && parseCode(smsText).isNotBlank()) {
            sign_up_password_input_otp.setText(parseCode(smsText))

            /*  var errorIcon: Drawable = resources.getDrawable(R.drawable.approved)
              errorIcon.setBounds(Rect(0, 0, errorIcon.getIntrinsicWidth(), errorIcon.getIntrinsicHeight()))
             // sign_up_password_input_otp.setError(null, errorIcon)
              sign_up_password_input_otp.setCompoundDrawables(null,null,errorIcon,null);
  */

            sign_up_password_input_otp.setCompoundDrawablesWithIntrinsicBounds(R.drawable.otp_password, 0, R.drawable.approved, 0);
            sign_up_mobile_input.setCompoundDrawablesWithIntrinsicBounds(R.drawable.phone_signup, 0, R.drawable.approved, 0);

            sign_up_email_input.isEnabled = false
            sign_up_mobile_input.isEnabled = false

        }

    }

    var onClickListener: View.OnClickListener = View.OnClickListener { view ->
        // Toast.makeText(view.context, "Terms of service Clicked", Toast.LENGTH_LONG).show()

        var b = Bundle()
        b.putString("take_url", "https://www.google.co.in/")
        val intent = Intent(this, WebPage::class.java)
        intent.putExtras(b)
        startActivity(intent)
    }

    var onClickListener11: View.OnClickListener = View.OnClickListener { view ->
        // Toast.makeText(view.context, "Privacy Policy Clicked", Toast.LENGTH_LONG).show()
        var b = Bundle()
        b.putString("take_url", "https://play.google.com/store")
        val intent = Intent(this, WebPage::class.java)
        intent.putExtras(b)
        startActivity(intent)

    }

    var onClickListener_button: View.OnClickListener = View.OnClickListener { view ->
        signupValidate()
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
        /*if (item!!.itemId == R.id.action_bar_signup) {
            signupValidate()
        }*/
        if (item!!.itemId == android.R.id.home) {
            finish()
        }
        return true
    }


    fun signupValidate() {
        var proceed = true
        if (first_name_input.isInvalid(getString(R.string.pls_enter_naem))) proceed = false
        if (sign_up_email_input.isInvalid(getString(R.string.email_prompt))) proceed = false
        if (!isValidEmail(sign_up_email_input.text)) proceed = false

        if (IS_GOOGLE.equals("false")) {
            if (sign_up_password_input.isInvalid(getString(R.string.input_password))) proceed = false
            if (sign_up_password_input_confirm.isInvalid(getString(R.string.input_password))) proceed = false
        }

        if (sign_up_password_input_otp.isInvalid(getString(R.string.enter_valid_otp))) proceed = false
        if (!sign_up_password_input_otp.text.trim().toString().equals(sharedPreferencesData!!.signup_otp.toString())) {
            sign_up_password_input_otp.error = getString(R.string.enter_valid_otp)
            proceed = false
        }

        if (IS_GOOGLE.equals("false")) {
            if (!sign_up_password_input.text.trim().toString().equals(sign_up_password_input_confirm.text.trim().toString())) {
                sign_up_password_input.error = getString(R.string.passwords_not_match)
                proceed = false
            }
        }

        if (!EditTextUtil.isValidEmail(sign_up_email_input.text.toString())) {
            sign_up_email_input.error = getString(R.string.email_prompt)
            proceed = false
        }
        if (proceed)
            signUpUser()
    }

    fun isValidEmail(target: CharSequence): Boolean {
        return !TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches()
    }

    fun EditText.isInvalid(error: String): Boolean {
        if (this.text.isNullOrBlank()) {
            this.error = error
            return true
        } else return false
    }

    private val eventobserver = object : Observer<SignUpEvent> {
        override fun onCompleted() {

            // sharedPreferencesData!!.saveSignupOTP("")
        }

        override fun onError(e: Throwable) {
            e.printStackTrace()
        }

        override fun onNext(event: SignUpEvent) {
            if (progressdialog!!.isShowing)
                progressdialog!!.dismiss()
            when (event.event) {
                Event.SERVER_ERROR -> showerrorDialog(event.message)
                Event.POST_FAILURE -> showerrorDialog(event.message)
                Event.POST_SUCCESS -> showSuccessDialog()
                else -> {
                }
            }

        }
    }

    private val otpeventobserver = object : Observer<SignupOTPEvent> {
        override fun onCompleted() = Unit

        override fun onError(e: Throwable) = e.printStackTrace()

        override fun onNext(event: SignupOTPEvent) {
            if (progressdialog!!.isShowing) progressdialog!!.dismiss()
            when (event.event) {
                Event.POST_SUCCESS -> {
                    //showSuccessDialog(getString(R.string.success_forgot11), 1)
                    Toast.makeText(context, getString(R.string.success_forgot11), Toast.LENGTH_LONG).show()


                }
                Event.POST_FAILURE -> showerrorDialog(event.error)

                Event.ERROR -> showSuccessDialog1(event.error)

                else -> {
                }
            }
        }
    }


    private fun signUpUser() {
        if (NetworkHelper.isOnline(this@SignUpActivity)) {
            progressdialog!!.show()
            if (sign_up_mobile_input.text.trim().length > 0)
                presenter!!.postdata(getSignUpModelPhone())
            else
                presenter!!.postdata(getSignUpModel())

        } else
            showerrorDialog(getString(R.string.noInternet))
    }

    private fun getSignUpModel(): SignUpModel {
        return SignUpModel(first_name_input.text.toString(), sign_up_email_input.text.toString().toLowerCase(),
                sign_up_password_input.text.toString(),
                getUsertype(), getCurrentDate(), USERTYPE_ID!!, GOOGLE_PERSION_ID!!
        )
    }

    private fun getSignUpModelPhone(): SignUpModel {
        return SignUpModel(first_name_input.text.toString(), sign_up_email_input.text.toString().toLowerCase(),
                sign_up_mobile_input.text.toString(),
                sign_up_password_input.text.toString(),
                getUsertype(), getCurrentDate(), USERTYPE_ID!!, GOOGLE_PERSION_ID!!
        )
    }

    private fun getSignUpOTPModel(): SignupOTPModel {
        return SignupOTPModel(sign_up_email_input.text.toString().toLowerCase(),"")
    }

    private fun getSignUpModelOTPPhone(): SignupOTPModel {
        return SignupOTPModel(sign_up_email_input.text.toString().toLowerCase(),
                sign_up_mobile_input.text.toString())
    }

    private fun getUsertype(): String {
        if (usertype_individual.isChecked) return "2"
        else if (usertype_parent.isChecked) return "5"

        else return "4"
    }

    private fun getCurrentDate(): String {
        val c = Calendar.getInstance()
        val df = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        return df.format(c.getTime())
    }

    /*1;"Admin"
     2;"Individual Teacher"
    `3;"Teacher"
     4;"Student"
     5;"Parent"
     6;"Staff"*/

private fun showerrorDialog(message: String?){}
    /*private fun showerrorDialog(message: String?) {
        var alert = AlertDialog.Builder(this@SignUpActivity)
        alert.setTitle(R.string.error)
        alert.setMessage(message)
        alert.setPositiveButton(R.string.btn_retry) { dialog, whichButton ->

            *//*if (IS_GOOGLE.equals("false"))
                signUpUser("3")
            else
                signUpUser("2")*//*

            signUpUser()

        }
        alert.setNegativeButton(R.string.btn_cancel) { dialog, whichButton ->
            dialog.dismiss()
        }
        alert.show()
    }*/

    private fun showSuccessDialog() {

        if (progressdialog!!.isShowing)
            progressdialog!!.dismiss()

        var alert = AlertDialog.Builder(this)
        alert.setTitle(R.string.success)
        alert.setMessage(R.string.registration_successful)
        alert.setPositiveButton(R.string.btn_ok) { dialog, whichButton ->
            dialog.dismiss()
            finish()
        }
        alert.show()
    }

    private fun showSuccessDialog1(message: String?) {

        if (progressdialog!!.isShowing)
            progressdialog!!.dismiss()

        var alert = AlertDialog.Builder(this)
        alert.setTitle(R.string.success)
        alert.setMessage(message)
        alert.setPositiveButton(R.string.btn_ok) { dialog, whichButton ->
            dialog.dismiss()
            // finish()
        }
        alert.show()
    }
}
