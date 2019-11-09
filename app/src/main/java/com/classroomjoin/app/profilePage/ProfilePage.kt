package com.classroomjoin.app.profilePage

import android.os.Bundle
import android.support.v4.view.ViewCompat
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import android.view.MotionEvent
import android.view.View
import com.akexorcist.localizationactivity.ui.LocalizationActivity
import com.classroomjoin.app.R
import com.classroomjoin.app.profileEditPage.ProfileEditPage
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_friend_details.*
import kotlinx.android.synthetic.main.friend_details_content.*
import kotlinx.android.synthetic.main.profile_detail_page.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.error
import org.jetbrains.anko.startActivity
import rx.Observable
import rx.Observer

class ProfilePage : LocalizationActivity(), AnkoLogger {

    val EXTRA_OBJCT = "com.app.sample.chatting"
    var viewmodel: ProfileViewModel? = null

    @SuppressWarnings("ConstantConditions")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_friend_details)

        ViewCompat.setTransitionName(findViewById(R.id.app_bar_layout), EXTRA_OBJCT)

        viewmodel = ProfileViewModel(this)

        setSupportActionBar(findViewById(R.id.toolbar) as Toolbar)
        getSupportActionBar()!!.setDisplayHomeAsUpEnabled(true)
        getSupportActionBar()!!.title = ""
        supportActionBar?.setTitle(getString(R.string.home))


    }

    private fun getObservable(): Observable.Transformer<ProfileUIEvent, ProfileUIModel> {
        val observable: Observable.Transformer<ProfileUIEvent, ProfileUIModel> = Observable.Transformer<ProfileUIEvent, ProfileUIModel> { profileUIEvent ->
            profileUIEvent
                    .flatMap { viewmodel!!.getProfileUiModel() }
        }

        return observable
    }

    override fun onResume() {
        super.onResume()
        Observable.just(ProfileUIEvent())
                .compose(getObservable())
                .subscribe(Observer())
    }

    private fun Observer(): Observer<ProfileUIModel> {
        return object : Observer<ProfileUIModel> {
            override fun onError(e: Throwable?) {
                e!!.printStackTrace()
            }

            override fun onNext(t: ProfileUIModel) {
                when (t) {
                    is Success -> setData(t)
                    is Failure -> showError(t.error)
                    is inProgress -> showProgress(true)
                }
            }

            override fun onCompleted() {
            }

        }
    }

    override fun dispatchTouchEvent(motionEvent: MotionEvent): Boolean {
        try {
            return super.dispatchTouchEvent(motionEvent)
        } catch (e: NullPointerException) {
            return false
        }

    }

    override fun onBackPressed() {
        super.onBackPressed()
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item!!.itemId == R.id.action_bar_edit) {
            startActivity<ProfileEditPage>()
        } else if (item.itemId == android.R.id.home) {
            finish()
        }
        return true
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.edit_menu, menu)
        return true
    }

    private fun showProgress(boolean: Boolean) {
        profile_page_content_loading_profile.visibility = if (boolean) View.VISIBLE else View.GONE
    }

    private fun showError(error: String) {
        showProgress(false)
        error_profile_page.visibility = View.VISIBLE
        text_error.text = error
    }

    private fun setData(data: Success) {
        error { data.contact_details }
        showProgress(false)

        if (data.img_url != null)
            Picasso.with(this).load(data.img_url).into(profile_image_meterial)

        collapsing_toolbar.setTitle(data.user_name)

        profile_username_text.text = (data.user_name)


        if (!data.gender.isNullOrBlank())
            profile_gender_text.text = data.gender
        else
            profile_address_email_lay.visibility = View.GONE

        if (!data.dob.isNullOrBlank())
            profile_dob_text.text = data.dob
        else
            profile_address_dob_lay.visibility = View.GONE

        if (!data.contact_details.isNullOrBlank())
            profile_address_text.text = data.contact_details
        else
            profile_address_text_lay.visibility = View.GONE

        profile_address_phone.text = data.phone

        profile_address_email.text = data.email

    }


}
