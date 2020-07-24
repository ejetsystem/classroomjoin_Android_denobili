package com.denobili.app.sign_step

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.fragment.NavHostFragment
import com.denobili.app.R
import kotlinx.android.synthetic.main.activity_signup_main.nav_host_fragment

class SignupMainActivity : AppCompatActivity() {
    companion object SignupMainActivity {
        var IS_GOOGLE: String = "com.classroom.is_google_login"
        var IS_EMAIL: String = "com.classroom.is_google_email"

    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup_main)
    }

    override fun onSupportNavigateUp() = NavHostFragment.findNavController(nav_host_fragment).navigateUp()

}
