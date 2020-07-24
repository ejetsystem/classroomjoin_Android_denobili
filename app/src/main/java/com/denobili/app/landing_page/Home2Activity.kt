package com.denobili.app.landing_page

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.support.v4.view.ViewPager
import android.support.v4.view.ViewPager.OnPageChangeListener
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Button
import android.widget.TextView
import com.denobili.app.R
import com.denobili.app.splashPage.LanguageSelectionActivity
import com.denobili.app.utils.SharedPreferencesData
import com.viewpagerindicator.CirclePageIndicator
import java.util.*

class Home2Activity  : AppCompatActivity() {

    private var mPager: ViewPager? = null
    private var started: Button? = null
    var skip: TextView? = null
    private var isLastPageSwiped = false
    private var counterPageScroll = 0
    var swipeTimer: Timer? = null
    var sharedPreferencesData: SharedPreferencesData? = null
    private var currentPage = 0
    private var NUM_PAGES = 0
    private val IMAGES = arrayOf(R.drawable.artboard6, R.drawable.artboard3, R.drawable.artboard2, R.drawable.artboard1, R.drawable.artboard4)
    private val ImagesArray = ArrayList<Int>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_slide)
        swipeTimer = Timer()
        sharedPreferencesData = SharedPreferencesData(this)
        sharedPreferencesData!!.welCome("isLaunch")
        started = findViewById<View>(R.id.started) as Button
        skip = findViewById<View>(R.id.skip) as TextView
        skip!!.setOnClickListener {
            if (sharedPreferencesData!!.user_languafe == null || sharedPreferencesData!!.user_languafe == "") {
                val intent = Intent(this@Home2Activity, LanguageSelectionActivity::class.java)
                intent.putExtra("is_from", true)
                startActivity(intent)
                finish()
            }
        }
        started!!.setOnClickListener {
            if (sharedPreferencesData!!.user_languafe == null || sharedPreferencesData!!.user_languafe == "") {
                val intent = Intent(this@Home2Activity, LanguageSelectionActivity::class.java)
                intent.putExtra("is_from", true)
                startActivity(intent)
                finish()
            }
        }
        init()
    }

    private fun init() {
        for (i in IMAGES.indices) ImagesArray.add(IMAGES[i])
        mPager = findViewById<View>(R.id.pager) as ViewPager
        mPager!!.adapter = SlidingImage_Adapter(this@Home2Activity, ImagesArray)
        val indicator = findViewById<View>(R.id.indicator) as CirclePageIndicator
        indicator.setViewPager(mPager)
        val density = resources.displayMetrics.density
        //Set circle indicator radius
        indicator.radius = 5 * density
        NUM_PAGES = IMAGES.size
        // Auto start of viewpager
        if (isLastPageSwiped) {
            swipeTimer!!.cancel()
        } else {
            runData()
            val handler = Handler()
            val Update = Runnable {
                if (currentPage == NUM_PAGES) {
                    currentPage = 0
                    started!!.visibility = View.GONE
                    skip!!.visibility = View.VISIBLE
                } else if (currentPage == 4) {
                    started!!.visibility = View.VISIBLE
                    skip!!.visibility = View.GONE
                } else {
                }
                mPager!!.setCurrentItem(currentPage++, true)
            }
            swipeTimer!!.schedule(object : TimerTask() {
                override fun run() {
                    handler.post(Update)
                }
            }, 1000, 1000)
        }
        // Pager listener over indicator
        indicator.setOnPageChangeListener(object : OnPageChangeListener {
            override fun onPageSelected(position: Int) {
                currentPage = position
            }

            override fun onPageScrolled(pos: Int, arg1: Float, arg2: Int) {
                if (pos == 4 && arg1 == 0f && !isLastPageSwiped) {
                    isLastPageSwiped = true

                    swipeTimer!!.cancel()
                    started!!.visibility = View.VISIBLE
                    skip!!.visibility = View.GONE
                    //Next Activity here
                    counterPageScroll++
                } else {
                    started!!.visibility = View.GONE
                    skip!!.visibility = View.VISIBLE
                    isLastPageSwiped = false
                    counterPageScroll = 0
                }
            }

            override fun onPageScrollStateChanged(pos: Int) {}
        })
    }

    private fun runData() {

    }
}

