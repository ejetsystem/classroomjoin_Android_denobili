package com.denobili.app.slide_login

import android.Manifest
import android.app.ProgressDialog
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.net.Uri
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
import com.denobili.app.R
import com.denobili.app.forgotPasswordPage.ForgotPasswordActivity
import com.denobili.app.helper.DialogUtil
import com.denobili.app.helper.NetworkHelper
import com.denobili.app.helper_utils.Event
import com.denobili.app.helper_utils.MainBus
import com.denobili.app.loadWebPage.WebPage
import com.denobili.app.loginPage.LoginEvent
import com.denobili.app.loginPage.LoginModel
import com.denobili.app.loginPage.LoginPresenter
import com.denobili.app.loginPage.loiginMicroService.LoginMicroEvent
import com.denobili.app.loginPage.loiginMicroService.LoginMicroPresenter
import com.denobili.app.utils.SharedPreferencesData
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import kotlinx.android.synthetic.main.final_sign.*
import kotlinx.android.synthetic.main.final_sign.forgot_password_button
import kotlinx.android.synthetic.main.final_sign.terms_of_service
import kotlinx.android.synthetic.main.first_step_sign.*
import kotlinx.android.synthetic.main.second_sign_step.*
import lt.neworld.spanner.Spanner
import lt.neworld.spanner.Spans
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.onClick
import org.jetbrains.anko.startActivity
import rx.Observer

class FirstStep : Fragment() ,View.OnClickListener , AnkoLogger{
    private var progressdialog: ProgressDialog? = null
    private var presenter: LoginPresenter? = null
    private var login_micro_presenter: LoginMicroPresenter? = null
    private var change_account = false
    var sharedPreferencesData: SharedPreferencesData? = null
    var personEmail: String? = null
    var personId: String? = null

    var is_google: String? = "false"
    var is_service: Boolean = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.first_step_sign, container, false)
    }
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        options.visibility=View.GONE
        presenter = LoginPresenter(activity!!)


        login_micro_presenter = LoginMicroPresenter(activity!!)
        sharedPreferencesData = SharedPreferencesData(activity)
        progressdialog = DialogUtil.showProgressDialog(activity!!)
        MainBus.getInstance().busObservable.ofType(LoginEvent::class.java).subscribe(eventobserver)
        MainBus.getInstance().busObservable.ofType(LoginMicroEvent::class.java).subscribe(eventobserver_micro)
        email_tap.setOnClickListener(this)
        phone_tab.setOnClickListener(this)
        nextToC.setOnClickListener(this)
        nextToC.setText("Login")
        user_email.addTextChangedListener(textWatcher)
        pass_word.addTextChangedListener(textWatcher)
        number.addTextChangedListener(textWatcher)
        password.addTextChangedListener(textWatcher)
        goBack.setOnClickListener(this)
       pass_linear.visibility=View.VISIBLE
        forgot_password_button.visibility=View.VISIBLE
        forgot_password_button1.visibility=View.VISIBLE
        emailLog.setText("Login")
        signTxt.setText("Sign In")
        val spannable = Spanner().append(getString(R.string.by_continuing))
                .append(getString(R.string.terms) + " ", Spans.click(onClickListener))
                .append(getString(R.string.and_add) + " ")
                .append(getString(R.string.privacy_policy), Spans.click(onClickListener11))
        terms_of_service.setMovementMethod(LinkMovementMethod());

        terms_of_service.text = spannable
        my_text_view.setMovementMethod(LinkMovementMethod());

        my_text_view.text = spannable
        Dexter.withActivity(activity)
                .withPermissions(
                        Manifest.permission.CAMERA,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                ).withListener(object : MultiplePermissionsListener {
                    override fun onPermissionsChecked(report: MultiplePermissionsReport) {/* ... */
                    }

                    override fun onPermissionRationaleShouldBeShown(permissions: List<PermissionRequest>, token: PermissionToken) {/* ... */
                    }
                }).check()
        forgot_password_button.onClick {
           activity?.startActivity<ForgotPasswordActivity>()
        }
        forgot_password_button1.onClick {
            activity?.startActivity<ForgotPasswordActivity>()
        }
        nextToC.onClick {
            if (number.text.trim().length > 0) {
                if (isValidMobile(number.text.toString())) {

                    if(number.text.toString().length==10) {

                        is_google = "false"

                        login_micro_presenter!!.postdata(number.text.trim().toString())

                    }else{

                        //  input_username_input.error = "Please Enter 10 digit Mobile No."

                        showErrorDialog("Please Enter 10 digit Mobile No.")

                    }

                } else {

                    number.error = getString(R.string.enter_valid_number)
                }
            } else
                number.isInvalid(getString(R.string.enter_valid_number))

        }
        emailLog.onClick {

            if (user_email.text.trim().length > 0) {

                if (isValidMail(user_email.text.toString())) {

                    is_google = "false"
                    personEmail = user_email.text.toString()
                    progressdialog!!.show()
                    login_micro_presenter!!.postdata(user_email.text.trim().toString().toLowerCase())

                }  else {

                    user_email.error = getString(R.string.enter_validEmail)
                }
            } else
                user_email.isInvalid(getString(R.string.enter_validEmail))
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
                Event.ERROR -> showSuccessDialog_SingIn(event.error)
                Event.SERVER_ERROR -> showErrorDialog(getString(R.string.serverError))
                Event.POST_FAILURE -> showErrorDialog(getString(R.string.serverError))
                Event.POST_SUCCESS -> {

                    if (!is_service) {

                        println("Data11--->" + "is_google--->" + is_google)

                        if (is_google.equals("false")){
                            if (number.text.toString().length==10){
                                loginValidate1()
                            }else
                            {
                                loginValidate()
                            }
                        }


                        else if (is_google.equals("true")) {

                            if (sharedPreferencesData!!.socialId != null) {

                                if (sharedPreferencesData!!.userTypeId == 1) {
                                    showSuccessDialog1(getString(R.string.admin_aert))
                                    return
                                }

                                presenter!!.postdata(LoginModel(personId, personId), change_account)

                            } else {
                                //signinGoogle_presenter!!.postdata(SignInGoogleModel(personEmail, personId, getCurrentDate()))
                            }
                        }
                    }
                }
                else -> {
                }
            }

        }

    }
    fun loginValidate1(){
        if (number.isInvalid(getString(R.string.enter_valid_number))) return

        if (password.isInvalid(getString(R.string.input_password))) return
        if (isValidMobile(number.text.toString())) {

            println("Data11--->else" + "isValidMobile")


        } else {

            number.error = getString(R.string.enter_valid_number)

            return
        }
        // }

        //println("Data11--->" + "userTypeId--->" + sharedPreferencesData!!.userTypeId)


        if (sharedPreferencesData!!.userTypeId == 1) {
            showSuccessDialog1(getString(R.string.admin_aert))
            return
        }
       loginMobile()
    }
    fun loginValidate() {
        //var proceed = true


        /* "1";"LinkedIn"
         "2";"Google"
         "3";"Manual"
         "4";"Facebook" */

        //if (is_google.equals("true")){

        println("Data11--->else" + "loginValidate--->" + user_email.text.toString())

        if (user_email.isInvalid(getString(R.string.enter_validEmail))) return

        if (pass_word.isInvalid(getString(R.string.input_password))) return

        if (isValidMail(user_email.text.toString())) {

            println("Data11--->else" + "isValidMail")

            personEmail = user_email.text.toString()
        }  else {

            user_email.error = getString(R.string.enter_validEmail)

            return
        }
        // }

        //println("Data11--->" + "userTypeId--->" + sharedPreferencesData!!.userTypeId)


        if (sharedPreferencesData!!.userTypeId == 1) {
            showSuccessDialog1(getString(R.string.admin_aert))
            return
        }
            loginUser()


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
    private val eventobserver = object : Observer<LoginEvent> {
        override fun onCompleted() {}

        override fun onError(e: Throwable) {
            e.printStackTrace()
        }
        override fun onNext(event: LoginEvent) {
            if (progressdialog!!.isShowing) progressdialog!!.dismiss()
            when (event.event) {
                Event.SERVER_ERROR -> DialogUtil.showerrorDialog(event.message, this@FirstStep::loginUser, activity!!)
                Event.POST_FAILURE -> DialogUtil.showerrorDialog(event.message, this@FirstStep!!::loginUser, activity!!)
                Event.POST_SUCCESS -> {
                    is_service = true
                   activity?.finishAffinity()
                }
                else -> {
                }
            }

        }
    }
    private fun showSuccessDialog1(message: String?) {

        if (progressdialog!!.isShowing)
            progressdialog!!.dismiss()

        var alert = android.app.AlertDialog.Builder(activity)
        alert.setTitle(R.string.app_name)
        alert.setMessage(message)
        alert.setPositiveButton(R.string.btn_redirect) { dialog, whichButton ->
            dialog.dismiss()
            // finish()

            /* var b = Bundle()
             b.putString("take_url", "http://getfirstcut.com/")
             val intent = Intent(this, WebPage::class.java)
             intent.putExtras(b)
             startActivity(intent)*/

            val httpIntent = Intent(Intent.ACTION_VIEW)
            httpIntent.data = Uri.parse("http://classroomjoin.com/")
            startActivity(httpIntent)
        }
        alert.setNegativeButton(R.string.btn_cancel) { dialog, whichButton ->
            dialog.dismiss()
            // finish()
        }
        alert.show()
    }
    private fun showSuccessDialog_SingIn(message: String?) {

        if (progressdialog!!.isShowing)
            progressdialog!!.dismiss()

        var alert = android.app.AlertDialog.Builder(activity)
        alert.setTitle(R.string.app_name)
        alert.setMessage(message)
        alert.setPositiveButton(R.string.btn_proceed) { dialog, whichButton ->
            dialog.dismiss()



        }
        alert.setNegativeButton(R.string.btn_cancel) { dialog, whichButton ->
            dialog.dismiss()
            // finish()
        }
        alert.show()
    }
    private fun loginMobile() {
        if (NetworkHelper.isOnline(activity)) {
            progressdialog!!.show()
            presenter!!.postdata(getLoginModel1(), change_account)
        } else
            DialogUtil.showerrorDialog(getString(R.string.noInternet), this::loginUser, activity!!)
    }
    private fun loginUser() {
        if (NetworkHelper.isOnline(activity)) {
            progressdialog!!.show()

            presenter!!.postdata(getLoginModel(), change_account)
        } else
            DialogUtil.showerrorDialog(getString(R.string.noInternet), this::loginUser, activity!!)
    }
    private fun getLoginModel1(): LoginModel {
        return LoginModel(number.text.toString().toLowerCase(),
                password.text.toString()
        )
    }
    private fun getLoginModel(): LoginModel {
        return LoginModel(user_email.text.toString().toLowerCase(),
                pass_word.text.toString()
        )
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

    override fun onClick(v: View?) {
        when(v?.getId()) {
            R.id.email_tap ->
                emailOption()
            R.id.phone_tab->
                phoneOption()
            R.id.goBack->
                activity?.finish()
        }
    }

    private fun phoneOption() {
        phone.visibility=View.VISIBLE
        emailoption.visibility=View.GONE
        user_email.setText("")
        pass_word.setText("")
        phone_tab.setBackgroundResource(R.drawable.bottom_rectangle)
        email_tap.setBackgroundResource(0)
    }
    private fun emailOption() {
        phone.visibility=View.GONE
        emailoption.visibility=View.VISIBLE
        number.setText("")
        password.setText("")
        email_tap.setBackgroundResource(R.drawable.bottom_rectangle)
        phone_tab.setBackgroundResource(0)
    }
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun checkFieldsForEmptyValues() {
     if (number.text.toString().length!=0&&password.text.toString().length!=0){
         nextToC.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#009688"))
     nextToC.setTextColor(Color.WHITE);}
     else
     {
         nextToC.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#ECECEC"))
         nextToC.setTextColor(Color.GRAY);
     }

        if (user_email.text.toString().length!=0&&pass_word.text.toString().length!=0){
            emailLog.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#009688"))
            emailLog.setTextColor(Color.WHITE);
        }
        else{
            emailLog.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#ECECEC"))
            emailLog.setTextColor(Color.GRAY);
        }


    }

    private fun showErrorDialog(message: String?) {

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
    var onClickListener: View.OnClickListener = View.OnClickListener { view ->
        // Toast.makeText(view.context, "Terms of service Clicked", Toast.LENGTH_LONG).show()

        var b = Bundle()
        b.putString("take_url", "https://www.classroomjoin.com/terms-and-conditions.html")
        val intent = Intent(activity, WebPage::class.java)
        intent.putExtras(b)
        startActivity(intent)
    }

    var onClickListener11: View.OnClickListener = View.OnClickListener { view ->
        // Toast.makeText(view.context, "Privacy Policy Clicked", Toast.LENGTH_LONG).show()
        var b = Bundle()
        b.putString("take_url", "https://www.classroomjoin.com/privacy-policy.html")
        val intent = Intent(activity, WebPage::class.java)
        intent.putExtras(b)
        startActivity(intent)

    }
}