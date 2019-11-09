package com.classroomjoin.app.studentDetailPage

import android.Manifest
import android.app.ProgressDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresPermission
import android.support.v4.app.ActivityCompat
import android.support.v7.app.AlertDialog
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.akexorcist.localizationactivity.ui.LocalizationActivity
import com.classroomjoin.app.R
import com.classroomjoin.app.attendancePage.AttendanceActivity
import com.classroomjoin.app.helper.DialogUtil
import com.classroomjoin.app.helper.NetworkHelper
import com.classroomjoin.app.helper_utils.Emit
import com.classroomjoin.app.helper_utils.Event
import com.classroomjoin.app.helper_utils.MainBus
import kotlinx.android.synthetic.main.page_fragment_setting.*
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.onClick
import org.jetbrains.anko.share
import rx.Observer

class StudentDetailPage : LocalizationActivity() {

    companion object StudentDetailObject {
        var STUDENT_ID_KEY: String = "com.classroom.student_id"
        var Student_Name_Key: String = "com.classroom.student_name_key"
        var CLASS_ID_KEY: String = "com.classroom.class_id_key"

    }

    private var progressdialog: ProgressDialog? = null
    private var classid: String? = "2"
    private var studentid: String? = null
    private var isloading: Boolean = false
    private var presenter: AddStudentPresenter? = null
    private var student_name: String = ""
    private var student_Code: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.page_fragment_setting)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_close_green_24dp)
        supportActionBar?.setDefaultDisplayHomeAsUpEnabled(true)
        supportActionBar?.setTitle(getString(R.string.home))

        progressdialog = DialogUtil.showProgressDialog(this@StudentDetailPage)
        MainBus.getInstance().busObservable.ofType(AddStudentEvent::class.java).subscribe(eventobserver)
        presenter = AddStudentPresenter(this@StudentDetailPage)
        studentid = intent.getStringExtra(STUDENT_ID_KEY)
        if (intent.hasExtra(Student_Name_Key)) {
            //supportActionBar?.title=intent.getStringExtra(Student_Name_Key)
            detail_student_name_short.setSolidColor((R.color.colorPrimary))
            detail_student_name_short.text = intent.getStringExtra(Student_Name_Key)
            //System.out.println("Data--->" + "getStringExtra--->" + intent.getStringExtra(Student_Name_Key))
        }

        if (intent.hasExtra(CLASS_ID_KEY))
            classid = intent.getStringExtra(CLASS_ID_KEY)
        //System.out.println("Data--->"+"CLASS_ID_KEY--->"+classid)


        if (studentid != null) {
            isloading = true
            getData()
        } else showcontent()

        detail_student_code.onClick {
            if (student_Code != null) share(student_Code!!, getString(R.string.student_code_share_text))
        }

        lyt_share.onClick {
            if (student_Code != null) share(student_Code!!, getString(R.string.student_code_share_text))
        }

        lyt_phone.onClick {

            @RequiresPermission
            if (detail_parent_mobile_number.text != null && !detail_parent_mobile_number.text.equals("")) {
                val callIntent = Intent(Intent.ACTION_CALL)
                callIntent.data = Uri.parse("tel:" + detail_parent_mobile_number.text)
                if (getPermissions())
                    startActivity(callIntent)
            }
        }

        lyt_email.onClick {

            val intent = Intent(Intent.ACTION_SEND)
            intent.type = "text/html"
            startActivity(Intent.createChooser(intent, "Send mail"))
        }

        student_attendance_detail.onClick {
            startActivity(intentFor<AttendanceActivity>(AttendanceActivity.studentidkey to studentid))
        }

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
                    System.out.println("Data--->" + "POST_SUCCESS--->")

                    if (event.studentDetail != null)
                        showresults(event.studentDetail)
                }
                Event.RESULT -> {
                    isloading = false
                    invalidateOptionsMenu()
                    System.out.println("Data--->" + "RESULT--->")

                    if (event.studentDetail != null)
                        showresults(event.studentDetail)
                }
                Event.NO_RESULT -> showerrorDialog(event.message)
                Event.NO_INTERNET -> showerrorDialog(getString(R.string.noInternet))
                else -> {
                }
            }

        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.edit_menu, menu)
        var menuitem_edit = menu!!.findItem(R.id.action_bar_edit)
        menuitem_edit!!.setVisible(!presenter!!.isAdmin())
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item!!.itemId == R.id.action_bar_edit) {
            startActivityForResult(intentFor<StudentDetailEditPage>(StudentDetailEditPage.STUDENT_ID_KEY to studentid, StudentDetailEditPage.CLASS_ID_KEY to classid), 2)
            finish()
        } else if (item.itemId == android.R.id.home) {
            finish()
        }
        return true

    }

    private fun getData() {
        if (NetworkHelper.isOnline(this)) {
            presenter?.getdata(studentid!!)
        } else Emit(AddStudentEvent(Event.NO_INTERNET))
    }

    private fun showresults(studentDetail: StudentDetail?) {
        showcontent()
        System.out.println("Data--->" + "showresults--->" + studentDetail!!.name)
        student_profile_name.text = studentDetail!!.name
        supportActionBar?.title = studentDetail!!.name
        var parent_name = ""

        if (studentDetail.parentname != null && !studentDetail.parentname.equals(""))
            parent_name = studentDetail.parentname

        if (studentDetail.parentMiddleName != null && !studentDetail.parentMiddleName.equals(""))
            parent_name = parent_name + " " + studentDetail.parentMiddleName

        if (studentDetail.parentLastName != null && !studentDetail.parentLastName.equals(""))
            parent_name = parent_name + " " + studentDetail.parentLastName

        detail_parent_name.text = parent_name
        detail_parent_email.text = studentDetail.parent_email
        detail_parent_mobile_number.text = studentDetail.mobile
        detail_student_code.text = studentDetail.studentCode
        student_Code = studentDetail.studentCode!!

    }

    private fun getPermissions(): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CALL_PHONE), 200)
            } else {
                return true
            }
            return false
        } else
            return true
    }

    private fun showcontent() {
        detail_page_content_loading.hide()
        detail_page_content_loading.visibility = View.GONE
        detail_content_card_new.visibility = View.VISIBLE
    }

    private fun showerrorDialog(message: String?) {
        var alert = AlertDialog.Builder(this@StudentDetailPage)
        alert.setTitle(R.string.error)
        alert.setMessage(message)
        alert.setPositiveButton(R.string.btn_retry) { dialog, whichButton ->
            getData()
            dialog.dismiss()

        }
        alert.setNegativeButton(R.string.btn_cancel) { dialog, whichButton ->
            dialog.dismiss()
        }
        alert.show()
    }

}
