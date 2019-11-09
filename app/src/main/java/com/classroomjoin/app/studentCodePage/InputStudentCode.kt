package com.classroomjoin.app.studentCodePage

import android.app.ProgressDialog
import android.os.Bundle
import android.support.design.widget.TextInputEditText
import android.support.design.widget.TextInputLayout
import android.support.v7.app.AlertDialog
import android.view.Menu
import android.view.MenuItem
import com.akexorcist.localizationactivity.ui.LocalizationActivity
import com.classroomjoin.app.R
import com.classroomjoin.app.helper.DialogUtil
import com.classroomjoin.app.helper.NetworkHelper
import com.classroomjoin.app.helper_utils.Event
import com.classroomjoin.app.helper_utils.MainBus
import kotlinx.android.synthetic.main.activity_input_student_code.*
import rx.Observer

class InputStudentCode : LocalizationActivity() {

    private var menuitem: MenuItem? = null
    private var progressdialog: ProgressDialog? = null
    private var presenter: InputStudentCodePresenter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_input_student_code)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_close_green_24dp)
        supportActionBar?.setDefaultDisplayHomeAsUpEnabled(true)
        supportActionBar?.setTitle(getString(R.string.addstudent))

        progressdialog = DialogUtil.showProgressDialog(this@InputStudentCode)
        MainBus.getInstance().busObservable.ofType(InputCodeEvent::class.java).subscribe(eventobserver)
        presenter = InputStudentCodePresenter(this@InputStudentCode)

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.send_page, menu)
        menuitem = menu.findItem(R.id.action_bar_send)
        return true
    }

    override fun onPrepareOptionsMenu(menu: Menu): Boolean {
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item!!.itemId == menuitem!!.itemId) {
            var proceed = true
            if (student_input_code_input.isInvalid(student_input_code_layout, getString(R.string.input_student_code_prompt))) proceed = false
            if (proceed) addStudent()
        }
        if (item!!.itemId == android.R.id.home) {
            finish()
        }
        return true
    }


    private val eventobserver = object : Observer<InputCodeEvent> {
        override fun onCompleted() {}

        override fun onError(e: Throwable) {
            e.printStackTrace()
        }

        override fun onNext(event: InputCodeEvent) {
            if (progressdialog!!.isShowing) progressdialog!!.dismiss()
            when (event.event) {
                Event.SERVER_ERROR -> showerrorDialog(event.message)
                Event.POST_FAILURE -> showerrorDialog(event.message)
                Event.POST_SUCCESS -> {
                    finish()
                }
                Event.RESULT -> {
                    invalidateOptionsMenu()
                }

                else -> {
                }
            }

        }
    }

    private fun addStudent() {
        if (NetworkHelper.isOnline(this@InputStudentCode)) {
            progressdialog!!.show()
            val addstudentmodel: InputCodeModel? = getStudentDetails()
            presenter!!.postdata(addstudentmodel!!)
        } else
            showerrorDialog(getString(R.string.noInternet))
    }

    private fun getStudentDetails(): InputCodeModel {
        return InputCodeModel(student_input_code_input.text.toString(), null, -1, "")
    }

    private fun showerrorDialog(message: String?) {
        var alert = AlertDialog.Builder(this@InputStudentCode);
        alert.setTitle(R.string.error)
        alert.setMessage(message)
        alert.setPositiveButton(R.string.btn_retry) { dialog, whichButton ->
            addStudent()
            dialog.dismiss()

        }
        alert.setNegativeButton(R.string.btn_cancel) { dialog, whichButton ->
            dialog.dismiss()
        }
        alert.show()
    }

    fun TextInputEditText.isInvalid(textInputLayout: TextInputLayout, error: String): Boolean {
        if (this.text.isNullOrBlank()) {
            textInputLayout.error = error
            return true
        } else return false
    }

}

