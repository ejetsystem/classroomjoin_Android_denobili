package com.denobili.app.splashPage

import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.akexorcist.localizationactivity.ui.LocalizationActivity
import com.denobili.app.R
import com.denobili.app.landing_page.LandingActivity
import com.denobili.app.utils.SharedPreferencesData
import kotlinx.android.synthetic.main.activity_language_selection.*
import org.jetbrains.anko.startActivity


class LanguageSelectionActivity : LocalizationActivity() {

    var sharedPreferencesData: SharedPreferencesData? = null
    var position11: Int = -1
    var is_from: Boolean = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_language_selection)
        sharedPreferencesData = SharedPreferencesData(this)

        spinner.setItems(getString(R.string.choode_one), getString(R.string.english), getString(R.string.hindi), getString(R.string.telugu))

        if (intent.hasExtra("is_from")) {
            is_from = intent.getBooleanExtra("is_from", false)
           /* if(sharedPreferencesData!!.user_languafe.equals(getString(R.string.english)))
            spinner.selectedIndex=0
            else if(sharedPreferencesData!!.user_languafe.equals(getString(R.string.hindi)))
                spinner.selectedIndex=1
            else if(sharedPreferencesData!!.user_languafe.equals(getString(R.string.telugu)))
                spinner.selectedIndex=2

*/
        }


        spinner.setOnItemSelectedListener { view, position, id, item ->

            position11 = position

            if (position != 0) {
                sharedPreferencesData!!.saveLanguage(item.toString())
                Toast.makeText(this@LanguageSelectionActivity, item.toString() + " selected", Toast.LENGTH_SHORT).show()
            } else {
                sharedPreferencesData!!.saveLanguage("")
            }
        }

        button_proceed.setOnClickListener(View.OnClickListener {

            if (sharedPreferencesData!!.user_languafe != null && !sharedPreferencesData!!.user_languafe.equals("")) {

                if (position11 == 2)
                    setLanguage("hi")
                else if (position11 == 3)
                    setLanguage("te")
                else if (position11 == 1)
                    setLanguage("en")
                if (is_from)
                    startActivity<LandingActivity>()
                finish()
            } else
                Toast.makeText(this@LanguageSelectionActivity, "Please select a language", Toast.LENGTH_SHORT).show()

        })


    }
}
