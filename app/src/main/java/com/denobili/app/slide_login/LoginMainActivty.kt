package com.denobili.app.slide_login

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.fragment.NavHostFragment
import com.denobili.app.R
import kotlinx.android.synthetic.main.activity_login_main_activty.*

class LoginMainActivty : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_main_activty)
    }
    override fun onSupportNavigateUp() = NavHostFragment.findNavController(nav_host_fragment).navigateUp()

}
