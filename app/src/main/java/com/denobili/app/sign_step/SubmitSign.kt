package com.denobili.app.sign_step

import android.app.AlertDialog
import android.app.ProgressDialog
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
import com.denobili.app.helper.DialogUtil
import com.denobili.app.helper.EditTextUtil
import com.denobili.app.helper.NetworkHelper
import com.denobili.app.helper_utils.Event
import com.denobili.app.helper_utils.MainBus
import com.denobili.app.landing_page.AccountOption
import com.denobili.app.loadWebPage.WebPage
import com.denobili.app.signUpPage.SignUpEvent
import com.denobili.app.signUpPage.SignUpModel
import com.denobili.app.signUpPage.SignUpPresenter
import com.denobili.app.sign_step.signup_manager.SignPresenter
import com.denobili.app.sign_step.signup_manager.SignupMobileModel
import com.denobili.app.utils.SharedPreferencesData
import com.thekhaeng.pushdownanim.PushDownAnim
import kotlinx.android.synthetic.main.submit_sign.*
import kotlinx.android.synthetic.main.submit_sign.close
import kotlinx.android.synthetic.main.submit_sign.nextToD
import kotlinx.android.synthetic.main.submit_sign.terms_of_service
import kotlinx.android.synthetic.main.submit_sign.usertype_individual
import kotlinx.android.synthetic.main.submit_sign.usertype_parent
import lt.neworld.spanner.Spanner
import lt.neworld.spanner.Spans
import rx.Observer
import java.text.SimpleDateFormat
import java.util.*

class SubmitSign : Fragment() {
    var userEmail:String?=null
    var is_google:String?=null
    var phoneNum:String?=null
    var otpMob:String?=null
    var passowrdTxt:String?=null
    var registerType:String?=null
    private var progressdialog: ProgressDialog? = null
    private var presenter: SignPresenter? = null
    private var presenter1: SignUpPresenter? = null
    var sharedPreferencesData: SharedPreferencesData? = null
    private var IS_GOOGLE: String = "false"
    private var GOOGLE_PERSION_ID: String? = null
    private var USERTYPE_ID: String? = null

    private var EMAIL_HAVE: String? = null
    companion object SubmitSign {
        var IS_GOOGLE: String = "com.classroom.is_google_login"
        var IS_EMAIL: String = "com.classroom.is_google_email"

    }
    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.submit_sign, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sharedPreferencesData = SharedPreferencesData(activity)
        usertype_parent.isChecked = true
        val spannable = Spanner().append(getString(R.string.by_continuing))
                .append(getString(R.string.terms) + " ", Spans.click(onClickListener))
                .append(getString(R.string.and_add) + " ")
                .append(getString(R.string.privacy_policy), Spans.click(onClickListener11))
        terms_of_service.setMovementMethod(LinkMovementMethod());

        terms_of_service.text = spannable

        user_name.addTextChangedListener(textWatcher)
        userEmail=arguments?.getString("email")
        otpMob=arguments?.getString("otp")
        phoneNum=arguments?.getString("mobile")
        passowrdTxt=arguments?.getString("password")
        is_google=arguments?.getString("is_google")
        if (is_google!=null&&sharedPreferencesData?.social_email!=""){
            registerType="3"
            email.setText(sharedPreferencesData?.social_email)
            email.isEnabled=false
            emailLin.visibility=View.GONE
            view11.visibility=View.GONE
            passowrdTxt=""
        }
        else{
            registerType="1"
        }

        MainBus.getInstance().busObservable.ofType(SignUpEvent::class.java).subscribe(eventobserver)
        progressdialog = DialogUtil.showProgressDialog(activity!!)
        presenter = SignPresenter(activity!!)
        presenter1 = SignUpPresenter(activity!!)
        PushDownAnim.setPushDownAnimTo(nextToD).setScale(PushDownAnim.MODE_STATIC_DP, 5F).setOnClickListener {
           if (user_name.isInvalid(getString(R.string.pls_enter_naem)))
           else if (email.isInvalid(getString(R.string.enter_validEmail)))
           else if (!EditTextUtil.isValidEmail(email.text.toString())) {
               email.error = getString(R.string.email_prompt)
           }
           else if (phoneNum!=null){
               signUpMobile()
           }
            else{
               signUpUser()
           }

        }
        PushDownAnim.setPushDownAnimTo(close).setScale(PushDownAnim.MODE_STATIC_DP, 5F).setOnClickListener {
            view.findNavController().popBackStack()
            // activity?.findViewById<Stepper>(R.id.Stepper)?.forward()
        }

    }

    private fun signUpMobile() {
        if (NetworkHelper.isOnline(activity)) {
            progressdialog!!.show()
            presenter1!!.postdata(getSignUpModel1())

        } else
            showerrorDialog(getString(R.string.noInternet))
    }

    private fun getSignUpModel1(): SignUpModel {
        return SignUpModel(user_name.text.toString(), email.text.toString().toLowerCase(),
                phoneNum.toString(),
                passowrdTxt.toString(),
                getUsertype(), getCurrentDate(), registerType.toString(), ""
        )
    }

    fun EditText.isInvalid(error: String): Boolean {
        if (this.text.isNullOrBlank()) {
            this.error = error
            return true
        } else return false
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
        if (user_name.text.toString().length!=0){
            nextToD.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#009688"))
            nextToD.setTextColor(Color.WHITE);}
        else
        {
            nextToD.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#ECECEC"))
            nextToD.setTextColor(Color.GRAY);
        }
    }

    private fun signUpUser() {
        if (NetworkHelper.isOnline(activity)) {
            progressdialog!!.show()
                presenter!!.postdata(getSignUpModel())

        } else
            showerrorDialog(getString(R.string.noInternet))
    }
    private fun showerrorDialog(message: String?){
        if (progressdialog!!.isShowing)
            progressdialog!!.dismiss()

        var alert = AlertDialog.Builder(activity)
        alert.setTitle(R.string.alert)
        alert.setMessage(message)
        alert.setPositiveButton(R.string.btn_ok) { dialog, whichButton ->
            dialog.dismiss()
        }
        alert.show()
    }
    private fun getSignUpModel(): SignupMobileModel {
        return SignupMobileModel(user_name.text.toString(), userEmail.toString().toLowerCase(),
                passowrdTxt.toString(),getUsertype(), getCurrentDate(), registerType.toString(), ""
        )
    }

    private fun getUsertype(): String {
        if (usertype_individual.isChecked) return "2"
        else if (usertype_parent.isChecked) return "5"
        else return "4"
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
    private fun showSuccessDialog() {

        if (progressdialog!!.isShowing)
            progressdialog!!.dismiss()

        var alert = AlertDialog.Builder(activity)
        alert.setTitle(R.string.success)
        alert.setMessage(R.string.registration_successful)
        alert.setPositiveButton(R.string.btn_ok) { dialog, whichButton ->
            dialog.dismiss()
           activity?.intent = Intent(activity, AccountOption::class.java)
            startActivity(activity?.intent)
            activity?.finishAffinity()
        }
        alert.show()
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
    private fun getCurrentDate(): String {
        val c = Calendar.getInstance()
        val df = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        return df.format(c.getTime())
    }

}