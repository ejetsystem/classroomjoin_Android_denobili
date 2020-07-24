package com.denobili.app.landing_page

import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.method.LinkMovementMethod
import android.view.View
import com.denobili.app.R
import com.denobili.app.helper.DialogUtil
import com.denobili.app.helper_utils.Event
import com.denobili.app.helper_utils.MainBus
import com.denobili.app.loadWebPage.WebPage
import com.denobili.app.loginPage.LoginEvent
import com.denobili.app.loginPage.LoginModel
import com.denobili.app.loginPage.LoginPresenter
import com.denobili.app.loginPage.loiginMicroService.LoginMicroEvent
import com.denobili.app.loginPage.loiginMicroService.LoginMicroPresenter
import com.denobili.app.loginPage.signupGoogle.SignInGoogleEvent
import com.denobili.app.loginPage.signupGoogle.SignInGoogleModel
import com.denobili.app.loginPage.signupGoogle.SigninGooglePresenter
import com.denobili.app.sign_step.FirstSignStep
import com.denobili.app.sign_step.SignupMainActivity
import com.denobili.app.utils.SharedPreferencesData
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.tasks.Task
import kotlinx.android.synthetic.main.signup_option.*
import kotlinx.android.synthetic.main.signup_option.close
import kotlinx.android.synthetic.main.signup_option.terms_of_service
import lt.neworld.spanner.Spanner
import lt.neworld.spanner.Spans
import org.jetbrains.anko.intentFor
import rx.Observer
import java.text.SimpleDateFormat
import java.util.*

class SignUp : AppCompatActivity(), View.OnClickListener, GoogleApiClient.OnConnectionFailedListener{
    var entryPoint:String?=null
    var mGoogleSignInClient: GoogleSignInClient? = null
    private var progressdialog: ProgressDialog? = null
    var  mGoogleApiClient: GoogleApiClient?=null;
    private var login_micro_presenter: LoginMicroPresenter? = null
    var personEmail: String? = null
    private var signinGoogle_presenter: SigninGooglePresenter? = null
    var personId: String? = null
    var is_google: String? = "false"
    var is_service: Boolean = false
    private var change_account = false
    private var presenter: LoginPresenter? = null
    var sharedPreferencesData: SharedPreferencesData? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.signup_option)
        val spannable = Spanner().append(getString(R.string.by_continuing))
                .append(getString(R.string.terms) + " ", Spans.click(onClickListener))
                .append(getString(R.string.and_add) + " ")
                .append(getString(R.string.privacy_policy), Spans.click(onClickListener11))
        terms_of_service.setMovementMethod(LinkMovementMethod());

        terms_of_service.text = spannable
        progressdialog = DialogUtil.showProgressDialog(this)
        entryPoint= intent.getStringExtra("firstEntry")
        getWindow().setBackgroundDrawable( ColorDrawable(Color.TRANSPARENT));
        MainBus.getInstance().busObservable.ofType(LoginEvent::class.java).subscribe(eventobserver)
        MainBus.getInstance().busObservable.ofType(LoginMicroEvent::class.java).subscribe(eventobserver_micro)
        MainBus.getInstance().busObservable.ofType(SignInGoogleEvent::class.java).subscribe(eventobserver_signin_google)
        presenter = LoginPresenter(this)
        signinGoogle_presenter = SigninGooglePresenter(this)
        sharedPreferencesData = SharedPreferencesData(this)
        sharedPreferencesData?.saveEmailId("")
        login_email.setOnClickListener(this)
        signinBtn.setOnClickListener(this)
        close.setOnClickListener(this)
        sign_google.setOnClickListener(this)
        var gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestProfile()
                .build()
        mGoogleApiClient =  GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)
        login_micro_presenter = LoginMicroPresenter(this)
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
                        //loginValidate()
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
    private val eventobserver = object : Observer<LoginEvent> {
        override fun onCompleted() {}

        override fun onError(e: Throwable) {
            e.printStackTrace()
        }
        override fun onNext(event: LoginEvent) {
            if (progressdialog!!.isShowing) progressdialog!!.dismiss()
            when (event.event) {
                //Event.SERVER_ERROR -> DialogUtil.showerrorDialog(event.message, this@FirstStep::loginUser, activity!!)
                //Event.POST_FAILURE -> DialogUtil.showerrorDialog(event.message, this@FirstStep!!::loginUser, activity!!)
                Event.POST_SUCCESS -> {
                    is_service = true
                    finishAffinity()
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
    private fun showSuccessDialog_SingIn(message: String?) {

        if (progressdialog!!.isShowing)
            progressdialog!!.dismiss()

        var alert = AlertDialog.Builder(this)
        alert.setTitle(R.string.app_name)
        alert.setMessage(message)
        alert.setPositiveButton(R.string.btn_proceed) { dialog, whichButton ->
            sharedPreferencesData?.saveEmailId(personEmail.toString())
            dialog.dismiss()

            startActivity(intentFor<SignupMainActivity>(FirstSignStep.IS_GOOGLE to is_google,
                    FirstSignStep.IS_EMAIL to personEmail))

        }
        alert.setNegativeButton(R.string.btn_cancel) { dialog, whichButton ->
            dialog.dismiss()
            // finish()
        }
        alert.show()
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
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        super.onActivityResult(requestCode, resultCode, data)
        val signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient)
        mGoogleApiClient?.clearDefaultAccountAndReconnect()
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
    override fun onClick(v: View?) {
        when(v?.getId()) {
            R.id.login_email ->
            {
                sharedPreferencesData?.saveEmailId("")
                signEmail()
            }

            R.id.close->
             closeActivity()
            R.id.sign_google->signGoogle()
            R.id.signinBtn->
                if (entryPoint.equals("1"))
                {
                    intent = Intent(this, AccountOption::class.java)
                    intent.putExtra("firstEntry","2")
                    startActivity(intent)
                }
                else{
                    finish()
                }

        }
    }

    private fun signGoogle() {
        val signInIntent = mGoogleSignInClient!!.getSignInIntent()
        startActivityForResult(signInIntent, 100)
    }

    private fun closeActivity() {
        finish();
        overridePendingTransition(R.anim.nothing, R.anim.slide_out_down);
    }

    private fun signEmail() {
        intent = Intent(this, SignupMainActivity::class.java)
        intent.putExtra("googleSign","1")
        startActivity(intent)
    }
    override fun onBackPressed() {
        closeActivity()
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

    override fun onConnectionFailed(p0: ConnectionResult) {

    }
}
