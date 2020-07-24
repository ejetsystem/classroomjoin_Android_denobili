package com.denobili.app.addSocialGrade

import android.app.ProgressDialog
import android.os.Bundle
import android.support.design.widget.TextInputEditText
import android.support.design.widget.TextInputLayout
import android.support.v7.app.AlertDialog
import android.view.Menu
import android.view.MenuItem
import com.akexorcist.localizationactivity.ui.LocalizationActivity
import com.denobili.app.R
import com.denobili.app.helper.DialogUtil
import com.denobili.app.helper.NetworkHelper
import com.denobili.app.mySocialGradesPage.AddSocialGradeModel
import com.denobili.app.mySocialGradesPage.MySocialGradesModel
import kotlinx.android.synthetic.main.activity_add_social_grade.*
import org.jetbrains.anko.onCheckedChange
import rx.Observer
import java.text.SimpleDateFormat
import java.util.*

class AddSocialGradePage : LocalizationActivity() {

    companion object SocialGrade {
        var IS_IN_EDIT_MODEKEY: String = "com.classroom.android"
        var SOCIAL_GRADE_ID_KEY: String = "com.classroom.social_grade_id"
        var SOCIAL_GRADE_MARK: String = "com.classroom.social_grade_mark"
        var SOCIAL_GRADE_HEADING: String = "com.classroom.social_grade_name"
        var SOCIAL_GRADE_TYPE: String = "com.classroom.social_grade_type"
    }

    private var delete_item: MenuItem? = null
    private var is_deletd: Boolean = false


    //var inEditMode: Boolean? = false

    //private var classid: String? = "2"
    private var socialGrade_id: String? = null
    // private var isloading: Boolean = false

    private var doneallbutton: MenuItem? = null
    private var progressdialog: ProgressDialog? = null

    private var social_grade_title: String = ""
    private var social_grade_points: String = ""
    private var social_grade_type: Int = 0
    private var viewmodel: AddSocialGradeViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_social_grade)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_close_green_24dp)
        supportActionBar?.setDefaultDisplayHomeAsUpEnabled(true)
        supportActionBar?.setTitle(getString(R.string.title_add_social_grade))

        chkState_social_grade.onCheckedChange { compoundButton, b ->
            if (b) social_grade_type_input.text = getString(R.string.positive_social_grade)
            else social_grade_type_input.text = getString(R.string.negative_social_grade)
        }

        progressdialog = DialogUtil.showProgressDialog(this@AddSocialGradePage)
        viewmodel = AddSocialGradeViewModel(this@AddSocialGradePage)

        if (intent.hasExtra(SOCIAL_GRADE_ID_KEY)) {

            println("Data--->" + "hasExtra(SOCIAL_GRADE_ID_KEY")

            socialGrade_id = intent.getStringExtra(SOCIAL_GRADE_ID_KEY)
            social_grade_title = intent.getStringExtra(SOCIAL_GRADE_HEADING)
            social_grade_points = intent.getStringExtra(SOCIAL_GRADE_MARK)
            social_grade_type = intent.getIntExtra(SOCIAL_GRADE_TYPE, 0)
            if (social_grade_type == 0) {
                chkState_social_grade.isChecked = false
            }
            social_grade_input.setText(social_grade_title)
            social_grade_mark_input.setText(social_grade_points)
        } else {
            println("Data--->" + "$$$$$$$$$$$$$$")

        }
    }


    private val Observer: Observer<AddSocialGradeResponse>
        get() = object : Observer<AddSocialGradeResponse> {
            override fun onCompleted() {
            }

            override fun onError(e: Throwable?) {
                showerrorDialog(getString(R.string.some_error))
                e?.printStackTrace()
            }

            override fun onNext(t: AddSocialGradeResponse?) {
                if (t?.status == "Success") {
                    showSuccessDialog()
                } else showerrorDialog(t!!.error_response)

            }
        }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.edit_done_menu, menu)
        return true
    }

    override fun onPrepareOptionsMenu(menu: Menu): Boolean {
        doneallbutton = menu.findItem(R.id.action_bar_done_all)
        delete_item = menu.findItem(R.id.action_bar_delete)

        if (intent.hasExtra(SOCIAL_GRADE_ID_KEY))
            delete_item!!.isVisible = true
        else
            delete_item!!.isVisible = false

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if (item.itemId == doneallbutton!!.itemId) {
            is_deletd = false
            var proceed = true
            if (social_grade_input.isInvalid(socialgradename_layout, getString(R.string.social_grade_name_error))) proceed = false
            if (social_grade_mark_input.isInvalid(social_grade_mark_layout, getString(R.string.social_grde_mark_error))) proceed = false
            if (proceed) addSocialGrade(true)
        }
        if (item.itemId == android.R.id.home) {
            finish()
        }

        if (item.itemId == delete_item!!.itemId) {
            is_deletd = true
            addSocialGrade(false)
        }
        return true
    }

    private fun showSuccessDialog() {
        val alert = AlertDialog.Builder(this)
        alert.setTitle(R.string.success)
        if (is_deletd) {
            alert.setMessage(R.string.success_delete_social_grade)

        } else {
            if (socialGrade_id == null) alert.setMessage(R.string.success_add_social_grade)
            else alert.setMessage(R.string.success_edit_social_grade)
        }

        alert.setPositiveButton(R.string.btn_ok) { dialog, whichButton ->
            dialog.dismiss()
            finish()
        }
        alert.show()
    }

    private fun addSocialGrade(add: Boolean) {
        if (NetworkHelper.isOnline(this@AddSocialGradePage)) {
            progressdialog!!.show()
            if (add) {
                if (intent.hasExtra(SOCIAL_GRADE_ID_KEY))
                    viewmodel!!.updateSocialGrade(getStudentDetails()).subscribe(Observer)
                else
                    viewmodel!!.addSocialGrade(getAddModel()).subscribe(Observer)
            } else {
                viewmodel!!.deleteSocialGrade(socialGrade_id).subscribe(Observer)

            }

        } else
            showerrorDialog(getString(R.string.noInternet))
    }


    private fun TextInputEditText.isInvalid(textInputLayout: TextInputLayout, error: String): Boolean {
        return if (this.text.isNullOrBlank()) {
            textInputLayout.error = error
            true
        } else false
    }

    private fun getStudentDetails(): MySocialGradesModel {
        var gradetype: Int? = 0//negative social grade

        if (chkState_social_grade.isChecked) gradetype = 1
        return if (socialGrade_id != null) {
            MySocialGradesModel(social_grade_input.text.toString(), socialGrade_id?.toInt(),
                    gradetype.toString(), Integer.parseInt(social_grade_mark_input.text.toString()), null, null)
        } else {
            MySocialGradesModel(social_grade_input.text.toString(), null,
                    gradetype.toString(), Integer.parseInt(social_grade_mark_input.text.toString()), null, null)
        }

    }


    private fun getCurrentDate(): String {
        val c = Calendar.getInstance()
        val df = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        return df.format(c.time)
    }

    private fun getAddModel(): AddSocialGradeModel {
        var gradetype: Int? = 0//negative social grade
        if (chkState_social_grade.isChecked) gradetype = 1

        return AddSocialGradeModel(social_grade_input.text.toString(),
                social_grade_mark_input.text.toString(), null, null,
                gradetype.toString(), getCurrentDate())
    }

    private fun showerrorDialog(message: String) {
        val alert = AlertDialog.Builder(this@AddSocialGradePage)
        alert.setTitle(R.string.error)
        alert.setMessage(message)
        alert.setPositiveButton(R.string.btn_retry) { dialog, whichButton ->
            addSocialGrade(true)
        }
        alert.setNegativeButton(R.string.btn_cancel) { dialog, whichButton ->
            dialog.dismiss()
        }
        alert.show()
    }
}
