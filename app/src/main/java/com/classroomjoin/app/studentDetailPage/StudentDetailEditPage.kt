package com.classroomjoin.app.studentDetailPage

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.TextInputEditText
import android.support.design.widget.TextInputLayout
import android.support.v4.content.LocalBroadcastManager
import android.support.v7.app.AlertDialog
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.akexorcist.localizationactivity.ui.LocalizationActivity
import com.classroomjoin.app.R
import com.classroomjoin.app.helper.DialogUtil
import com.classroomjoin.app.helper.NetworkHelper
import com.classroomjoin.app.helper_utils.Event
import com.classroomjoin.app.helper_utils.MainBus
import kotlinx.android.synthetic.main.content_student_detail.*
import org.jetbrains.anko.toast
import rx.Observer
import java.text.SimpleDateFormat
import java.util.*


class StudentDetailEditPage : LocalizationActivity() {

    companion object StudentDetailObject {
        var IS_IN_EDIT_MODEKEY: String = "com.classroom.android"
        var STUDENT_ID_KEY: String = "com.classroom.student_id"
        var CLASS_ID_KEY: String = "com.classroom.class_id_key"
    }

    private var doneallbutton: MenuItem? = null
    private var delete_button: MenuItem? = null
    private var progressdialog: ProgressDialog? = null
    private var classid: String = "2"
    private var studentid: String? = null
    private var isloading: Boolean = false
    private var isadd: Boolean = false
    private var presenter: AddStudentPresenter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.content_student_detail)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_close_green_24dp)
        supportActionBar?.setDefaultDisplayHomeAsUpEnabled(true)
        supportActionBar?.setTitle(getString(R.string.title_detail))

        progressdialog = DialogUtil.showProgressDialog(this@StudentDetailEditPage)
        MainBus.getInstance().busObservable.ofType(AddStudentEvent::class.java).subscribe(eventobserver)
        presenter = AddStudentPresenter(this@StudentDetailEditPage)

        if (intent.hasExtra(CLASS_ID_KEY)) {
            classid = intent.getStringExtra(CLASS_ID_KEY)
            System.out.println("Data--->" + "StudentDetailEditPage CLASS_ID_KEY--->" + classid)
        }

        if (intent.hasExtra(STUDENT_ID_KEY)) {
            studentid = intent.getStringExtra(STUDENT_ID_KEY)
            isloading = true
            presenter?.getdata(studentid!!)
           // parent_mobile_layout.visibility=View.INVISIBLE

        } else showcontent()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.edit_done_menu, menu)
        return true
    }

    override fun onPrepareOptionsMenu(menu: Menu): Boolean {
        doneallbutton = menu.findItem(R.id.action_bar_done_all)
        delete_button = menu.findItem(R.id.action_bar_delete)
        doneallbutton?.isVisible = !isloading
        delete_button?.isVisible = (!isloading && studentid != null)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if (item.itemId == doneallbutton!!.itemId) {
            var proceed = true

            if (admission_id_input.isInvalid(admission_id_layout, getString(R.string.admission_id_prompt))) proceed = false
            if (student_name_input.isInvalid(student_name_layout, getString(R.string.prompt_student_name))) proceed = false
            if (roll_no_input.isInvalid(roll_no_layout, getString(R.string.prompt_roll_no))) proceed = false

            if (proceed) addStudent()

        } else if (item.itemId == android.R.id.home) {
            finish()
        } else if (item.itemId == delete_button!!.itemId) {
            deletStudent()
        }
        return true
    }


    private val eventobserver = object : Observer<AddStudentEvent> {
        override fun onCompleted() {}

        override fun onError(e: Throwable) {
            e.printStackTrace()
        }

        override fun onNext(event: AddStudentEvent) {
            if (progressdialog!!.isShowing) progressdialog!!.dismiss()
            when (event.event) {
                Event.SERVER_ERROR -> showerrorDialog(event.message)
                Event.POST_FAILURE -> showerrorDialog(event.message)
                Event.POST_SUCCESS -> {
                    //setResult(2)
                    sendMessage()
                    finish()
                }
                Event.RESULT -> {
                    isloading = false
                    invalidateOptionsMenu()
                    showresults(event.studentDetail)
                }
                Event.NO_RESULT -> shownoresults(event.message)
                Event.DELETED_ITEM -> {
                    toast(getString(R.string.deletion_success))
                    finish()
                }
                Event.DELETION_FAILURE -> showerrorDialog2(event.message)
                else -> {
                }
            }

        }
    }

    private fun sendMessage() {
        Log.d("sender", "Broadcasting message")
        val intent = Intent("custom-event-name")
        // You can also include some extra data.
        //intent.putExtra("message", "This is my message!")
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent)
    }

    private fun showresults(studentDetail: StudentDetail?) {
        showcontent()
        student_name_input.setText(studentDetail!!.name)
        roll_no_input.setText(studentDetail.rollno.toString())
        admission_id_input.setText(studentDetail.admission.toString())
        //parent_name_input.setText(studentDetail.parentname)
        //parent_email_input.setText(studentDetail.parent_email)
        //parent_mobile_input.setText(studentDetail.mobile)
    }

    private fun showcontent() {

        student_detail_content_loading.hide()
        student_detail_content_loading.visibility = View.GONE
        student_detail_message_textview.visibility = View.GONE
        student_detail_imageview.visibility = View.GONE

        student_name_layout.visibility = View.VISIBLE
        roll_no_layout.visibility = View.VISIBLE
        //parent_name_layout.visibility = View.VISIBLE
        // parent_email_layout.visibility = View.VISIBLE
        admission_id_layout.visibility = View.VISIBLE
        // parent_mobile_layout.visibility = View.VISIBLE
    }

    private fun shownoresults(error: String?) {
        student_name_layout.visibility = View.GONE
        roll_no_layout.visibility = View.GONE
        //parent_name_layout.visibility = View.GONE
        // parent_email_layout.visibility = View.GONE
        admission_id_layout.visibility = View.GONE
        // parent_mobile_layout.visibility = View.GONE

        student_detail_content_loading.hide()
        student_detail_content_loading.visibility = View.GONE

        student_detail_message_textview.visibility = View.VISIBLE
        student_detail_message_textview.text = error
        student_detail_imageview.visibility = View.VISIBLE

    }

    fun TextInputEditText.editable(isEditable: Boolean) {
        if (isEditable) this.isFocusableInTouchMode = true
        else this.isFocusable = false
    }

    private fun addStudent() {
        if (NetworkHelper.isOnline(this@StudentDetailEditPage)) {
            progressdialog!!.show()
            val addstudentmodel: AddStudentModel? = getStudentDetails()
            if (studentid != null) addstudentmodel?.student_id = studentid!!.toInt()
            presenter!!.postdata(addstudentmodel!!)
        } else
            showerrorDialog(getString(R.string.noInternet))
    }


    private fun deletStudent() {
        if (NetworkHelper.isOnline(this@StudentDetailEditPage)) {
            progressdialog!!.show()
            presenter!!.deleteitem(studentid!!)
        } else
            showerrorDialog2(getString(R.string.noInternet))
    }

    private fun getStudentDetails(): AddStudentModel {
        return AddStudentModel(student_name_input.text.toString(),
                admission_id_input.text.toString(),
                roll_no_input.text.toString(),
                classid,
                getCurrentDate(),
                parent_mobile_input.text.toString())
    }

    private fun showerrorDialog(message: String?) {
        var alert = AlertDialog.Builder(this@StudentDetailEditPage)
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

    private fun showerrorDialog2(message: String?) {
        var alert = AlertDialog.Builder(this@StudentDetailEditPage)
        alert.setTitle(R.string.error)
        alert.setMessage(message)
        alert.setPositiveButton(R.string.btn_retry) { dialog, whichButton ->
            deletStudent()
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

    private fun getCurrentDate(): String {
        val c = Calendar.getInstance()
        val df = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        return df.format(c.getTime())
    }

}
