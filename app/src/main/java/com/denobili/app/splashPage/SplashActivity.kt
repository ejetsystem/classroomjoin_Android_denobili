package com.denobili.app.splashPage

import android.content.SharedPreferences
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.Window
import android.view.WindowManager
import com.akexorcist.localizationactivity.ui.LocalizationActivity
import com.denobili.app.R
import com.denobili.app.homepage.HomePage
import com.denobili.app.landing_page.Home2Activity
import com.denobili.app.landing_page.LandingActivity
import com.denobili.app.myConnectedStudents.MyConnectedStudentsListingActivity
import com.denobili.app.utils.SharedPreferencesData
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.startActivity


class SplashActivity : LocalizationActivity() {

    private val ACCESSTOKENKEY = "com.classroom.accesstoken"
    private val USERTYPE_KEY = "com.classroom.setting.usertype"
    private val LANGUAGE_KEY = "com.classroom.language"
    var sharedPreferencesData: SharedPreferencesData? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // For hiding title bar
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.splash_screen_page)
        sharedPreferencesData = SharedPreferencesData(this)

        val sharedPreferences: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(applicationContext)
        val background = object : Thread() {
            override fun run() {

                try {
                    Thread.sleep((1 * 1000).toLong())
                   if (sharedPreferencesData!!.onBoard==null){
                       startActivity<Home2Activity>()
                       finish()
                   }
                    else{
                       if (sharedPreferencesData!!.user_languafe == null || sharedPreferencesData!!.user_languafe.equals("")) {
                           startActivity(intentFor<LanguageSelectionActivity>("is_from" to true))
                           finish()
                       } else if (sharedPreferences?.getString(ACCESSTOKENKEY, null) == null) {
                           startActivity<LandingActivity>()
                           finish()
                       } else {
                           val usertype = sharedPreferences!!.getInt(USERTYPE_KEY, 1)
                           if (usertype == 1 || usertype == 2 || usertype == 3)
                               startActivity<HomePage>()
                           else startActivity<MyConnectedStudentsListingActivity>()
                           finish()
                       }
                   }

                } catch (e: Exception) {

                }

            }
        }

        background.start()
    }


}
