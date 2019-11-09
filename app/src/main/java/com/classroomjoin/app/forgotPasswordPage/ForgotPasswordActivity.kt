package com.classroomjoin.app.forgotPasswordPage

import android.app.ProgressDialog
import android.content.Context
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.text.Editable
import android.text.TextWatcher
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.Toast
import com.classroomjoin.app.R
import com.classroomjoin.app.helper.DialogUtil
import com.classroomjoin.app.helper.NetworkHelper
import com.classroomjoin.app.helper_utils.Event
import com.classroomjoin.app.helper_utils.MainBus
import com.classroomjoin.app.loginPage.loiginMicroService.LoginMicroEvent
import com.classroomjoin.app.loginPage.loiginMicroService.LoginMicroPresenter
import com.classroomjoin.app.otpReaderLibrary.OTPListenerForgotPassword
import com.classroomjoin.app.otpReaderLibrary.OtpReader
import com.classroomjoin.app.passwordConfirmation.ConfirmPassEvent
import com.classroomjoin.app.passwordConfirmation.ConfirmPassModel
import com.classroomjoin.app.passwordConfirmation.ConfirmPassPresenter
import com.classroomjoin.app.utils.SharedPreferencesData
import kotlinx.android.synthetic.main.activity_forgot_password.*
import rx.Observer
import java.util.regex.Pattern


class ForgotPasswordActivity : AppCompatActivity(), OTPListenerForgotPassword {

    private var progressdialog: ProgressDialog? = null
    private var presenter: ForgotPasswordPresenter? = null
    private var password_confirm_presenter: ConfirmPassPresenter? = null

    private var email: String? = null
    private var navigation: Int? = 0
    private var context: Context? = null
    private var sharedPreferencesData: SharedPreferencesData? = null
    private var login_micro_presenter: LoginMicroPresenter? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)
        OtpReader.bind(this, "BLOSSM")

        context = this

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_arrow_back_green_24dp)
        supportActionBar?.setDefaultDisplayHomeAsUpEnabled(true)
        supportActionBar?.setTitle(getString(R.string.forgot_password))

        sharedPreferencesData = SharedPreferencesData(this)

        progressdialog = DialogUtil.showProgressDialog(this)

        presenter = ForgotPasswordPresenter(this)
        password_confirm_presenter = ConfirmPassPresenter(this)
        login_micro_presenter = LoginMicroPresenter(this)

        MainBus.getInstance().busObservable.ofType(ForgotPasswordEvent::class.java).subscribe(eventobserver)
        MainBus.getInstance().busObservable.ofType(ConfirmPassEvent::class.java).subscribe(confirmpassobserver)
        MainBus.getInstance().busObservable.ofType(LoginMicroEvent::class.java).subscribe(eventobserver_micro)

        button_forgot_submit.setOnClickListener(onClickListener_button)
        button_forgot_regenerate_otp.setOnClickListener(onClickListener_otp)
        confirm_submit.setOnClickListener(onClickListener_submit)

        input_forgot_email.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable) = Unit

            override fun beforeTextChanged(s: CharSequence, start: Int,
                                           count: Int, after: Int) = Unit

            override fun onTextChanged(s: CharSequence, start: Int,
                                       before: Int, count: Int) = if (s.isEmpty())
                button_forgot_submit.setBackgroundResource(R.drawable.capsule_button2)
            else
                button_forgot_submit.setBackgroundResource(R.drawable.capsule_button)
        })

        input_forgot_otp.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable) = Unit

            override fun beforeTextChanged(s: CharSequence, start: Int,
                                           count: Int, after: Int) = Unit

            override fun onTextChanged(s: CharSequence, start: Int,
                                       before: Int, count: Int) = if (s.isEmpty())
                button_forgot_submit.setBackgroundResource(R.drawable.capsule_button2)
            else
                button_forgot_submit.setBackgroundResource(R.drawable.capsule_button)
        })

        input_forgot_confirmpassword.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable) = Unit

            override fun beforeTextChanged(s: CharSequence, start: Int,
                                           count: Int, after: Int) = Unit

            override fun onTextChanged(s: CharSequence, start: Int,
                                       before: Int, count: Int) =
                    if (s.isEmpty() && input_forgot_password.text.isEmpty())
                        confirm_submit.setBackgroundResource(R.drawable.capsule_button2)
                    else
                        confirm_submit.setBackgroundResource(R.drawable.capsule_button)
        })


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
        println("Data22--->" + parseCode(smsText))
        if (parseCode(smsText).isNotEmpty() && parseCode(smsText).isNotBlank()) {
            input_forgot_otp.setText(parseCode(smsText))
            button_forgot_submit.performClick()
        }

    }


    private var onClickListener_otp: View.OnClickListener = View.OnClickListener { view ->

        if (input_forgot_email.text.isNotBlank() && input_forgot_email.text.isNotEmpty()) {
            postdata()
        } else {
            showerrorDialog(getString(R.string.enter_username), 0)
        }
    }

    private var onClickListener_button: View.OnClickListener = View.OnClickListener { view ->

        if (navigation == 0) {


            /*  if (input_forgot_email.text.isNotBlank() && input_forgot_email.text.isNotEmpty())
                  postdata()
              else
                  showerrorDialog(getString(R.string.enter_username), 0)
  */


            if (input_forgot_email.text.trim().length > 0) {

                if (isValidMail(input_forgot_email.text.toString())) {

                    login_micro_presenter!!.postdata(input_forgot_email.text.trim().toString())


                } else if (isValidMobile(input_forgot_email.text.toString())) {

                    login_micro_presenter!!.postdata(input_forgot_email.text.trim().toString())


                } else {

                    input_forgot_email.error = getString(R.string.enter_username)

                }
            } else
                input_forgot_email.isInvalid(getString(R.string.enter_username))

        } else {

            if (input_forgot_otp.text.isNotEmpty() && input_forgot_email.text.isNotEmpty()) {
                postdataOTP()
            } else {
                //input_forgot_email_layout.error=getString(R.string.class_name_invalid)
                showerrorDialog(getString(R.string.enter_valid_otp), 0)

            }

        }
    }

    fun EditText.isInvalid(error: String): Boolean {
        if (this.text.isNullOrBlank()) {
            this.error = error
            return true
        } else return false
    }

    private fun isValidMail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun isValidMobile(phone: String): Boolean {
        return android.util.Patterns.PHONE.matcher(phone).matches()
    }

    private var onClickListener_submit: View.OnClickListener = View.OnClickListener { view ->

        if (input_forgot_password.text.isNotEmpty() && input_forgot_confirmpassword.text.isNotEmpty()) {

            if (input_forgot_password.text.trim().toString() == input_forgot_confirmpassword.text.trim().toString())
                postdataConfirm()
            else
                showerrorDialog(getString(R.string.passwords_not_match), 0)

        } else {
            //input_forgot_email_layout.error=getString(R.string.class_name_invalid)
            showerrorDialog(getString(R.string.input_password), 0)

        }
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        }

        return true
    }

    private fun postdataOTP() = if (NetworkHelper.isOnline(this)) {
        //progressdialog!!.show()
        email = input_forgot_email.text.toString()

        if (input_forgot_otp.text.trim().toString() == sharedPreferencesData!!.forgot_otp.toString()) {
            otp_details.visibility = View.GONE
            password_details.visibility = View.VISIBLE
            input_forgot_email_saved.setText(email)
            input_forgot_email_saved.keyListener = null

        } else {
            showerrorDialog(getString(R.string.enter_valid_otp), 2)

        }
    } else
        showerrorDialog(getString(R.string.noInternet), 2)

    private fun postdata() = if (NetworkHelper.isOnline(this)) {

        if (sharedPreferencesData!!.userTypeId == 1) {
            showSuccessDialog1(getString(R.string.admin_aert))
        }else {
            progressdialog!!.show()

            presenter!!.postdata((input_forgot_email.text.trim().toString()))
        }


    } else
        showerrorDialog(getString(R.string.noInternet), 1)

    private fun postdataConfirm() = if (NetworkHelper.isOnline(this)) {
        progressdialog!!.show()
        email = input_forgot_email.text.toString()
        password_confirm_presenter!!.postdata(ConfirmPassModel(input_forgot_email.text.toString(), input_forgot_password.text.toString()))
    } else
        showerrorDialog(getString(R.string.noInternet), 3)


    private val eventobserver = object : Observer<ForgotPasswordEvent> {
        override fun onCompleted() = Unit

        override fun onError(e: Throwable) = e.printStackTrace()

        override fun onNext(event: ForgotPasswordEvent) {
            if (progressdialog!!.isShowing) progressdialog!!.dismiss()
            when (event.event) {
                Event.POST_SUCCESS -> {
                    //showSuccessDialog(getString(R.string.success_forgot11), 1)
                    Toast.makeText(context, getString(R.string.success_forgot11), Toast.LENGTH_LONG).show()
                    button_forgot_regenerate_otp.visibility = View.VISIBLE
                    input_forgot_otp.visibility = View.VISIBLE
                    navigation = -1

                }
                Event.POST_FAILURE -> showerrorDialog(event.error, 1)

                Event.ERROR -> showSuccessDialog1(event.error)
                else -> {
                }
            }
        }
    }

    private val eventobserver_micro = object : Observer<LoginMicroEvent> {
        override fun onCompleted() {}

        override fun onError(e: Throwable) {
            e.printStackTrace()
        }

        override fun onNext(event: LoginMicroEvent) {
            if (progressdialog!!.isShowing) progressdialog!!.dismiss()
            when (event.event) {
                Event.ERROR -> showerrorDialog(event.error, 1)
                Event.SERVER_ERROR -> showSuccessDialog1(getString(R.string.admin_aert))
                Event.POST_FAILURE -> showSuccessDialog1(getString(R.string.admin_aert))
                Event.POST_SUCCESS -> postdata()
                else -> {
                }
            }

        }

    }

    private val confirmpassobserver = object : Observer<ConfirmPassEvent> {
        override fun onCompleted() = Unit

        override fun onError(e: Throwable) = e.printStackTrace()

        override fun onNext(event: ConfirmPassEvent) {
            if (progressdialog!!.isShowing) progressdialog!!.dismiss()
            when (event.event) {
                Event.POST_SUCCESS -> showSuccessDialog(getString(R.string.password_changed), 0)
                Event.ERROR -> showSuccessDialog1(event.error)
                Event.POST_FAILURE -> showerrorDialog(event.error, 3)
                else -> {
                }
            }
        }
    }


    private fun showSuccessDialog(message: String, type: Int) {
        val alert = AlertDialog.Builder(this)
        alert.setTitle(getString(R.string.success))
        alert.setMessage(message)
        alert.setPositiveButton(getString(R.string.btn_ok)) { dialog, whichButton ->

            if (type == 0) {
                finish()
            } else {
                //finish()

            }
        }
        alert.show()
    }

    private fun showerrorDialog(message: String?, type: Int) {
        val alert = AlertDialog.Builder(this)
        alert.setTitle(getString(R.string.error))
        alert.setMessage(message)

        if (type == 0) {

            alert.setPositiveButton(getString(R.string.btn_ok)) { dialog, whichButton ->
                //finish()
            }

        } else {
            alert.setPositiveButton(getString(R.string.btn_retry)) { dialog, whichButton ->

                when (type) {
                    1 -> postdata()
                    2 -> postdataOTP()
                    else -> postdataConfirm()
                }


            }
            alert.setNegativeButton(getString(R.string.btn_cancel)) { dialog, whichButton ->
                dialog.dismiss()
            }
        }
        alert.show()
    }

    private fun showSuccessDialog1(message: String?) {

        if (progressdialog!!.isShowing)
            progressdialog!!.dismiss()

        val alert = android.app.AlertDialog.Builder(this)
        alert.setTitle(R.string.success)
        alert.setMessage(message)
        alert.setPositiveButton(R.string.btn_ok) { dialog, whichButton ->
            dialog.dismiss()
            // finish()
        }
        alert.show()
    }


}
