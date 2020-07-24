package com.denobili.app.loginPage

import android.Manifest
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.design.widget.TextInputEditText
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.EditText
import com.denobili.app.R
import com.denobili.app.forgotPasswordPage.ForgotPasswordActivity
import com.denobili.app.helper.DialogUtil
import com.denobili.app.helper.NetworkHelper
import com.denobili.app.helper_utils.Event
import com.denobili.app.helper_utils.MainBus
import com.denobili.app.loginPage.loiginMicroService.LoginMicroEvent
import com.denobili.app.loginPage.loiginMicroService.LoginMicroPresenter
import com.denobili.app.loginPage.signupGoogle.SignInGoogleEvent
import com.denobili.app.loginPage.signupGoogle.SignInGoogleModel
import com.denobili.app.loginPage.signupGoogle.SigninGooglePresenter
import com.denobili.app.signUpPage.SignUpActivity
import com.denobili.app.signUpPage.SignUpModel
import com.denobili.app.utils.SharedPreferencesData
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import kotlinx.android.synthetic.main.login_page.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.onClick
import org.jetbrains.anko.startActivity
import rx.Observer
import java.text.SimpleDateFormat
import java.util.*


class LoginActivity : AppCompatActivity(), View.OnClickListener, AnkoLogger {

    private var progressdialog: ProgressDialog? = null
    private var presenter: LoginPresenter? = null
    private var login_micro_presenter: LoginMicroPresenter? = null
    private var signinGoogle_presenter: SigninGooglePresenter? = null

    private var email: String? = null
    private var mobile_number: String? = null
    private var change_account = false
    var sharedPreferencesData: SharedPreferencesData? = null
    var mGoogleSignInClient: GoogleSignInClient? = null
    var personEmail: String? = null
    var personId: String? = null

    var is_google: String? = "false"
    var is_service: Boolean = false

    companion object SwapAccount {
        val SWAP_KEY = "com.classroom.join.login_swap"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_page)

        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        var gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestProfile()
                .build()

        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)

        sign_in_button.setOnClickListener(this)
        progressdialog = DialogUtil.showProgressDialog(this@LoginActivity)

        MainBus.getInstance().busObservable.ofType(LoginEvent::class.java).subscribe(eventobserver)
        MainBus.getInstance().busObservable.ofType(LoginMicroEvent::class.java).subscribe(eventobserver_micro)
        MainBus.getInstance().busObservable.ofType(SignInGoogleEvent::class.java).subscribe(eventobserver_signin_google)

        presenter = LoginPresenter(this)
        login_micro_presenter = LoginMicroPresenter(this)
        signinGoogle_presenter = SigninGooglePresenter(this)

        sharedPreferencesData = SharedPreferencesData(this)

        Dexter.withActivity(this)
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


        button_login_submit.onClick {

            if (input_username_input.text.trim().length > 0) {

                if (isValidMail(input_username_input.text.toString())) {

                    is_google = "false"
                    personEmail = input_username_input.text.toString()

                    login_micro_presenter!!.postdata(input_username_input.text.trim().toString().toLowerCase())

                } else if (isValidMobile(input_username_input.text.toString())) {

                    if(input_username_input.text.toString().length==10) {

                        is_google = "false"

                        login_micro_presenter!!.postdata(input_username_input.text.trim().toString())

                    }else{

                      //  input_username_input.error = "Please Enter 10 digit Mobile No."

                        showErrorDialog("Please Enter 10 digit Mobile No.")

                    }

                } else {

                    input_username_input.error = getString(R.string.enter_username)
                }
            } else
                input_username_input.isInvalid(getString(R.string.enter_username))
        }

        button_signUp.onClick {
            //startActivity<SignUpActivity>()
            startActivity(intentFor<SignUpActivity>(SignUpActivity.IS_GOOGLE to "false", SignUpActivity.IS_EMAIL to null))

        }
        privacy.onClick {
            val uris=Uri.parse("https://elasticbeanstalk-us-west-2-784563199592.s3-us-west-2.amazonaws.com/terms_privacy/privacy_policy.html")
        val intent=Intent(Intent.ACTION_VIEW,uris)
            startActivity(intent)

        }

        forgot_password_button.onClick {
            startActivity<ForgotPasswordActivity>()
        }


    }

    override fun onClick(v: View) {
        when (v.getId()) {
            R.id.sign_in_button -> signIn()
        }// ...
    }

    private fun signIn() {
        val signInIntent = mGoogleSignInClient!!.getSignInIntent()
        startActivityForResult(signInIntent, 100)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        super.onActivityResult(requestCode, resultCode, data)
        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == 100) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        }
    }


    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val acct = completedTask.getResult(ApiException::class.java)
            //val acct = GoogleSignIn.getLastSignedInAccount(this)
            if (acct != null) {
                val personName = acct.displayName
                val personGivenName = acct.givenName
                val personFamilyName = acct.familyName
                personEmail = acct.email

                personId = acct.id
                val personPhoto = acct.photoUrl

                println("Data11--->handleSignInResult" + personId + "--" + acct.idToken)

                is_google = "true"

                login_micro_presenter!!.postdata(personEmail!!)

            } else {

                println("Data11--->handleSignInResult" + "acct != null")

                showSuccessDialog1(getString(R.string.serverError))

            }

            // Signed in successfully, show authenticated UI.
            // updateUI(account)
        } catch (e: ApiException) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            // Log.w(TAG, "signInResult:failed code=" + e.getStatusCode())
            // updateUI(null)

            e.printStackTrace()

            showSuccessDialog1(getString(R.string.serverError))

        }
    }


    fun loginValidate() {
        //var proceed = true


        /* "1";"LinkedIn"
         "2";"Google"
         "3";"Manual"
         "4";"Facebook" */

        //if (is_google.equals("true")){

        println("Data11--->else" + "loginValidate--->" + input_username_input.text.toString())

        if (input_username_input.isInvalid(getString(R.string.enter_username))) return

        if (input_password.isInvalid(getString(R.string.input_password))) return

        if (isValidMail(input_username_input.text.toString())) {

            println("Data11--->else" + "isValidMail")

            personEmail = input_username_input.text.toString()


        } else if (isValidMobile(input_username_input.text.toString())) {

            println("Data11--->else" + "isValidMobile")


        } else {

            input_username_input.error = getString(R.string.enter_username)

            return
        }
        // }

        println("Data11--->" + "userTypeId--->" + sharedPreferencesData!!.userTypeId)


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
                Event.SERVER_ERROR -> DialogUtil.showerrorDialog(event.message, this@LoginActivity::loginUser, this@LoginActivity)
                Event.POST_FAILURE -> DialogUtil.showerrorDialog(event.message, this@LoginActivity::loginUser, this@LoginActivity)
                Event.POST_SUCCESS -> {
                    is_service = true
                    finish()
                }
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
                Event.ERROR -> showSuccessDialog_SingIn(event.error)
                Event.SERVER_ERROR -> showErrorDialog(getString(R.string.serverError))
                Event.POST_FAILURE -> showErrorDialog(getString(R.string.serverError))
                Event.POST_SUCCESS -> {

                    if (!is_service) {

                        println("Data11--->" + "is_google--->" + is_google)

                        if (is_google.equals("false"))
                            loginValidate()
                        else if (is_google.equals("true")) {

                            if (sharedPreferencesData!!.socialId != null) {

                                if (sharedPreferencesData!!.userTypeId == 1) {
                                    showSuccessDialog1(getString(R.string.admin_aert))
                                    return
                                }

                                presenter!!.postdata(LoginModel(personId, personId), change_account)

                            } else {
                                signinGoogle_presenter!!.postdata(SignInGoogleModel(personEmail, personId, getCurrentDate()))
                            }
                        }
                    }
                }
                else -> {
                }
            }

        }

    }


    private val eventobserver_signin_google = object : Observer<SignInGoogleEvent> {
        override fun onCompleted() {}

        override fun onError(e: Throwable) {
            e.printStackTrace()
        }

        override fun onNext(event: SignInGoogleEvent) {
            if (progressdialog!!.isShowing) progressdialog!!.dismiss()
            when (event.event) {
                Event.ERROR -> showErrorDialog(event.error)
                Event.SERVER_ERROR -> showErrorDialog(getString(R.string.serverError))
                Event.POST_FAILURE -> showErrorDialog(getString(R.string.serverError))
                Event.POST_SUCCESS -> {

                    if (sharedPreferencesData!!.userTypeId == 1) {
                        showSuccessDialog1(getString(R.string.admin_aert))
                        return
                    }

                    presenter!!.postdata(LoginModel(personId, personId), change_account)

                }
                else -> {
                }
            }

        }

    }

    private fun getCurrentDate(): String {
        val c = Calendar.getInstance()
        val df = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        return df.format(c.getTime())

    }

    fun TextInputEditText.editable(isEditable: Boolean) {
        if (isEditable) this.isFocusableInTouchMode = true
        else this.isFocusable = false
    }

    private fun loginUser() {
        if (NetworkHelper.isOnline(this@LoginActivity)) {
            progressdialog!!.show()

            presenter!!.postdata(getLoginModel(), change_account)


            /* if (sharedPreferencesData!!.registrationTypeId == 3)
                 presenter!!.postdata(getLoginModel(), change_account)
             else if (sharedPreferencesData!!.registrationTypeId == 2)
                 presenter!!.google_postdata(getGoogleLoginModel(), change_account)*/

        } else
            DialogUtil.showerrorDialog(getString(R.string.noInternet), this::loginUser, this)
    }

    private fun getLoginModel(): LoginModel {
        return LoginModel(input_username_input.text.toString().toLowerCase(),
                input_password.text.toString()
        )
    }


/* private fun getGoogleLoginModel(): GoogleLoginModel {
     return GoogleLoginModel(personEmail,
             "2"
     )
 }*/

    private fun signUpUser() {
        if (NetworkHelper.isOnline(this@LoginActivity)) {
            progressdialog!!.show()
            presenter!!.postRegister(getSignUpModel(), change_account)
        } else
            DialogUtil.showerrorDialog(getString(R.string.noInternet), this::signUpUser, this@LoginActivity)
    }

    private fun getSignUpModel(): SignUpModel {
        var signupmodel = SignUpModel(email, mobile_number)
        //signupmodel.socialId=socialid
        return signupmodel
    }

    private fun showSuccessDialog1(message: String?) {

        if (progressdialog!!.isShowing)
            progressdialog!!.dismiss()

        var alert = android.app.AlertDialog.Builder(this)
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

        var alert = android.app.AlertDialog.Builder(this)
        alert.setTitle(R.string.app_name)
        alert.setMessage(message)
        alert.setPositiveButton(R.string.btn_proceed) { dialog, whichButton ->

            dialog.dismiss()

            startActivity(intentFor<SignUpActivity>(SignUpActivity.IS_GOOGLE to is_google,
                    SignUpActivity.IS_EMAIL to personEmail))

        }
        alert.setNegativeButton(R.string.btn_cancel) { dialog, whichButton ->
            dialog.dismiss()
            // finish()
        }
        alert.show()
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

}
