package com.denobili.app.profileEditPage

import agency.tango.android.avatarview.loader.PicassoLoader
import android.app.Activity
import android.app.DatePickerDialog
import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import com.akexorcist.localizationactivity.ui.LocalizationActivity
import com.denobili.app.R
import com.denobili.app.helper.DialogUtil
import com.denobili.app.helper.NetworkHelper
import com.denobili.app.profilePage.*
import kotlinx.android.synthetic.main.activity_profile_edit_page.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.onClick
import rx.Observable
import rx.Observer
import java.text.SimpleDateFormat
import java.util.*


class ProfileEditPage : LocalizationActivity(), AnkoLogger {

    //private var datePicker: DatePicker? = null
    private var calendar: Calendar? = null
    var viewmodel: ProfileViewModel? = null
    private var year: Int = 0
    var month: Int = 0
    var day: Int = 0

    var progressDialog: ProgressDialog? = null


    companion object Keys {
        var PROFILE_KEY = "com.classroom.profile_pic"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_edit_page)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_close_green_24dp)
        supportActionBar?.setDefaultDisplayHomeAsUpEnabled(true)
        supportActionBar?.setTitle(getString(R.string.profile_edit))

        viewmodel = ProfileViewModel(this)

        progressDialog = DialogUtil.showProgressDialog(this@ProfileEditPage)

        calendar = Calendar.getInstance();
        year = calendar!!.get(Calendar.YEAR);
        month = calendar!!.get(Calendar.MONTH);
        day = calendar!!.get(Calendar.DAY_OF_MONTH);

        date_picker.onClick {
            if (dob_label.text.toString() != getString(R.string.dummy_dob)) {
                val dob = dob_label.text.split("-")
                year = dob[0].toInt()
                month = dob[1].toInt()
                day = dob[2].toInt()
            }
            DatePickerDialog(this,
                    myDateListener, year, month, day).show()
        }

        profile_image_new.onClick {
            navigateProfileEdit()
        }

        edit_profile_pic.onClick {
            navigateProfileEdit()
        }
    }

    private fun navigateProfileEdit() {
        startActivityForResult(intentFor<ProfileImageChooserActivity>(), 123)
    }


    private val myDateListener = DatePickerDialog.OnDateSetListener { arg0, arg1, arg2, arg3 ->
        // arg1 = year
        // arg2 = month
        // arg3 = day
        showDate(arg1, arg2 + 1, arg3)
    }


    private fun showDate(year: Int, month: Int, day: Int) {
        val m = if (month < 10) "0" + month.toString() else month.toString()
        val d = if (day < 10) "0" + day.toString() else day.toString()
        dob_label.setText(StringBuilder().append(year).append("-")
                .append(m).append("-").append(d))
    }

    private fun getObservable(): Observable.Transformer<ProfileUIEvent, ProfileEditUIModel> {
        val observable: Observable.Transformer<ProfileUIEvent, ProfileEditUIModel>
                = Observable.Transformer<ProfileUIEvent, ProfileEditUIModel> { profileUIEvent ->
            profileUIEvent
                    .flatMap { viewmodel!!.postAction() }
                    .map { t: ProfileResult ->
                        when (t) {
                            is onSucces -> onEditSuccess(t.data.imageUrl,
                                    t.data.gnder,
                                    t.data.dob,
                                    t.data.firstName,
                                    t.data.middleName,
                                    t.data.lastName,
                                    t.data.alternateMobileNo,
                                    t.data.lindLine,
                                    t.data.add_1,
                                    t.data.add_2,
                                    t.data.city,
                                    t.data.state,
                                    t.data.country)
                            is onFailure -> onEditFailure(t.error)
                            is inFlight -> inProgress()
                        }
                    }
        }
        return observable
    }

    private fun showError(error: String) {
        detail_contents.visibility = View.GONE
        profile_page_content_loading.hide()
        image_error.visibility = View.VISIBLE
        text_error.visibility = View.VISIBLE
        text_error.text = error

    }

    override fun onResume() {
        super.onResume()
        Observable.just(ProfileUIEvent())
                .compose(getObservable())
                .subscribe(Observer())
    }

    private fun Observer(): Observer<ProfileEditUIModel> {
        return object : Observer<ProfileEditUIModel> {
            override fun onError(e: Throwable?) {
                e!!.printStackTrace()
            }

            override fun onNext(t: ProfileEditUIModel) {
                when (t) {
                    is onEditSuccess -> setData(t)
                    is onEditFailure -> showError(t.error)
                    is inProgress -> showProgress(true)
                }
            }

            override fun onCompleted() {
            }

        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.done_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item!!.itemId == R.id.action_bar_done_single) {
            postdata()
        } else if (item.itemId == android.R.id.home) {
            finish()
        }
        return true
    }

    private fun postdata() {
        if (NetworkHelper.isOnline(this@ProfileEditPage)) {
            validateData()
        } else DialogUtil.showerrorDialog(getString(R.string.noInternet), this@ProfileEditPage::postdata, this@ProfileEditPage)

    }

    fun validateData() {
        var proceed = true
        if (first_name_profile.isInvalid(getString(R.string.enter_valid_firstname))) proceed = false
        if (proceed) {
            progressDialog!!.show()
            viewmodel!!.postData(getUserInput()).subscribe(getPostObserver())
        }

    }

    fun EditText.isInvalid(error: String): Boolean {
        if (this.text.isNullOrBlank()) {
            this.error = error
            return true
        } else return false
    }


    private fun getPostObserver(): Observer<ProfilePostReponse> {
        return object : Observer<ProfilePostReponse> {
            override fun onNext(t: ProfilePostReponse) {
                progressDialog!!.dismiss()
                if (t.status == "Success") {

                    // Snackbar.make(address_line2, getString(R.string.success_edit_profile), Snackbar.LENGTH_SHORT).show()
                    finish()

                }
            }

            override fun onError(e: Throwable?) {
                progressDialog!!.dismiss()
                e!!.printStackTrace()
            }

            override fun onCompleted() {
            }

        }
    }

    private fun showProgress(b: Boolean) {
        image_error.visibility = View.GONE
        text_error.visibility = View.GONE
        if (!b) profile_page_content_loading.hide()
        profile_page_content_loading.visibility = if (b) View.VISIBLE else View.GONE
    }

    private fun getUserInput(): ProfileData {
        return ProfileData(first_name_profile.text.toString(),
                middile_name.text.toString(),
                last_name.text.toString(),
                AddDOB(),
                if (gender_spinner.selectedItemPosition != 0) gender_spinner.selectedItem.toString() else null,
                addresline1.text.toString(),
                address_line2.text.toString(),
                city_input.text.toString(),
                state_input.text.toString(),
                country_input.text.toString(),
                altername_moble.text.toString(),
                landline_number.text.toString(),
                viewmodel!!.org_id,
                viewmodel!!.userid, "",
                getCurrentDate(),"","")
    }

    private fun getCurrentDate(): String {
        val c = Calendar.getInstance()
        val df = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        return df.format(c.getTime())
    }

    private fun AddDOB(): String {
        if(dob_label.text.toString()!="YYYY-MM-DD")
            return dob_label.text.toString()
        else return ""


    }

    private fun setData(t: onEditSuccess) {

        showProgress(false)

        detail_contents.visibility = View.VISIBLE

        PicassoLoader().loadImage(profile_image_new, t.img_url, viewmodel!!.user_name)

        if (t.gender != null) {
            if (t.gender == "Male") gender_spinner.setSelection(1)
            else if (t.gender == "Female") gender_spinner.setSelection(2)
            else if (t.gender == "Other") gender_spinner.setSelection(3)
            else gender_spinner.setSelection(0)
        }

        if (!t.dob.isNullOrBlank()) dob_label.setText(t.dob)


        first_name_profile.setText(t.firstName)
        middile_name.setText(t.middleName)
        last_name.setText(t.lastName)

        altername_moble.setText(t.alternateMobileNo)
        landline_number.setText(t.lindLine)

        addresline1.setText(t.address1)
        address_line2.setText(t.address2)
        city_input.setText(t.city)
        state_input.setText(t.state)
        country_input.setText(t.country)


    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (data != null) PicassoLoader().loadImage(profile_image_new, data.getStringExtra(PROFILE_KEY), viewmodel!!.user_name)
        }
    }


}
