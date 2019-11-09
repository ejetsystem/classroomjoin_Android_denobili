package com.classroomjoin.app.teacherHomePage


import android.app.ProgressDialog
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.content.LocalBroadcastManager
import android.support.v7.content.res.AppCompatResources
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.classroomjoin.app.R
import com.classroomjoin.app.helper.DialogUtil
import com.classroomjoin.app.helper_utils.*
import com.classroomjoin.app.mystudentsPage.MyStudentModel
import com.classroomjoin.app.mystudentsPage.Sortbyname
import com.classroomjoin.app.mystudentsPage.Sortbyroll
import com.classroomjoin.app.postAttendancePage.AttendenceSelectionListingActivity
import com.classroomjoin.app.studentDetailPage.StudentDetailEditPage
import com.classroomjoin.app.studentListingPage.StudentListingAdapter
import com.classroomjoin.app.studentListingPage.StudentListingEvent
import com.classroomjoin.app.studentListingPage.StudentListingPresenter
import com.classroomjoin.app.teacherMessageSendPage.TeacherCommunicationPage
import kotlinx.android.synthetic.main.activity_plus_fab.*
import org.jetbrains.anko.*
import org.jetbrains.anko.collections.forEachWithIndex
import rx.Observer
import java.lang.Exception
import java.util.*


class TeacherHomePage : BaseListActivity(), AnkoLogger, StudentSelectionListener {

    override fun selectedStudent(position: Int, isSelected: Boolean) {
        var student: MyStudentModel = adapter!!.getItem(position) as MyStudentModel
        student.isSelected = isSelected
        adapter?.notifyItemChanged(position, student)
    }

    private var adapter: StudentListingAdapter? = null
    private var presenter: StudentListingPresenter? = null
    private var classid: String? = "0"
    private var displaylist: ArrayList<DisplayItem>? = ArrayList<DisplayItem>()
    private var referencelist: List<DisplayItem>? = ArrayList<DisplayItem>()
    private var select_all: MenuItem? = null
    private var deSelectall: MenuItem? = null
    private var menuitem_add: MenuItem? = null
    private var progressdialog: ProgressDialog? = null

    companion object TeacherHomePage_Companion {
        val CLASSIDKEY: String = "com.students.class"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_plus_fab)
        initviews()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_arrow_back_green_24dp)
        supportActionBar?.setTitle(getString(R.string.home))

        LocalBroadcastManager.getInstance(this).registerReceiver(broadCastReceiver,
                IntentFilter("custom-event-name"))

        progressdialog = DialogUtil.showProgressDialog(this@TeacherHomePage)
        MainBus.getInstance().busObservable.ofType(StudentListingEvent::class.java).subscribe(eventobserver)
        presenter = StudentListingPresenter(this)

        if (intent.hasExtra(CLASSIDKEY)) {
            classid = intent.getStringExtra(CLASSIDKEY)
        }
        // System.out.println("Data--->"+"TEACHER CLASS_ID_KEY--->"+classid)
        adapter = StudentListingAdapter(displaylist!!, this@TeacherHomePage, classid)
        recyclerView.adapter = adapter
        recyclerView.isNestedScrollingEnabled = false

        fab.image = AppCompatResources.getDrawable(this, R.drawable.ic_done_white_24dp)
        fab.visibility = View.GONE
        fab.onClick {
            if (getSelectedStudents().isNotEmpty())
                startActivity(intentFor<TeacherCommunicationPage>(
                        TeacherCommunicationPage.studentListKey to getSelectedStudents(),
                        TeacherCommunicationPage.parentListKey to getSelectedParents(),
                        TeacherCommunicationPage.CLASSIDKEY to classid))
            else
                showSnackbar(getString(R.string.select_students_prompt))
        }
        getData()


    }


    val broadCastReceiver = object : BroadcastReceiver() {
        override fun onReceive(contxt: Context?, intent: Intent?) {
            refreshData()
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        LocalBroadcastManager.getInstance(this)
                .unregisterReceiver(broadCastReceiver)
    }


    /*private fun getSelectedStudents(): ArrayList<Int> {
        var list = ArrayList<Int>()
        adapter!!.items.forEachWithIndex { i, displayItem ->
            var student: MyStudentModel = adapter!!.getItem(i) as MyStudentModel
            *//*if (student.isSelected!!)*//* list.add(student.id)
        }
        return list
    }*/

    private fun getSelectedStudents():ArrayList<Int>{
        var list=ArrayList<Int>()
        adapter!!.items.forEachWithIndex { i, displayItem ->
            var student: MyStudentModel = adapter!!.getItem(i) as MyStudentModel
            if(student.isSelected!!)list.add(student.id)
        }
        return list
    }

    private fun getSelectedParents(): ArrayList<Int> {
        var list = ArrayList<Int>()
        adapter!!.items.forEachWithIndex { i, displayItem ->
            var student: MyStudentModel = adapter!!.getItem(i) as MyStudentModel
            println("Data--->"+student.parentId)
            list.add(student.parentId)
        }
        return list
    }

    override fun onRefresh() {
        super.onRefresh()
        refreshData()
    }

    private fun refreshData() {
        showLoading()
        presenter!!.refreshdata(classid!!)
    }

    private val eventobserver = object : Observer<StudentListingEvent> {
        override fun onCompleted() {}

        override fun onError(e: Throwable) {
            e.printStackTrace()
        }

        override fun onNext(event: StudentListingEvent) {
            if (swipeContainer.isRefreshing) swipeContainer.isRefreshing = false
            if (progressdialog!!.isShowing) progressdialog!!.dismiss()
            when (event.event) {
                Event.RESULT -> {
                    referencelist = event.results
                    fab.visibility = View.VISIBLE
                    adapter!!.addItems(true, referencelist!!)
                    showResults()
                }
                Event.NO_RESULT ->
                    if (adapter!!.itemCount == 0) showNoResults(getString(R.string.no_students_added))
            // else showSnackbar(getString(R.string.noResults))
                Event.NO_INTERNET -> {
                    if (adapter!!.itemCount == 0) showNoInternet()
                    else showSnackbar(getString(R.string.noInternet))
                }
                Event.SERVER_ERROR -> if (adapter!!.itemCount == 0) showServerError()
                Event.ERROR -> if (adapter!!.itemCount == 0) showError(event.error)

                else -> {
                }
            }
        }
    }

    private fun showLoading() {
        imageView.visibility = View.GONE
        recyclerView.visibility = View.GONE
        textView.visibility = View.GONE
        contentLoadingProgressBar.visibility = View.VISIBLE
        contentLoadingProgressBar.show()
    }

    private fun showSnackbar(message: String) {
        Snackbar.make(recyclerView, message, Snackbar.LENGTH_SHORT).show()
    }

    override fun onResume() {
        super.onResume()
    }

    private fun getData() {
        showLoading()
        presenter!!.getdata(classid!!)
    }

    /*private fun selectAll(select: Boolean) {

        var unselected: Int = 0
        adapter?.items?.forEachWithIndex { i, displayItem ->
            var student: MyStudentModel = displayItem as MyStudentModel
            if (student.parentMobile != null && !student.parentMobile.equals(""))
                student.isSelected = select
            else
                unselected = unselected + 1
            adapter?.notifyItemChanged(i, student)
        }

        if (unselected > 0) {

            if (select)
                showSnackbar(getString(R.string.unselected_ones))
        }
    }*/

    private fun selectAll(select:Boolean){
        adapter?.items?.forEachWithIndex { i, displayItem ->
            var student: MyStudentModel =displayItem as MyStudentModel
            student.isSelected=select
            adapter?.notifyItemChanged(i,student)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.student__home_, menu)
        select_all = menu.findItem(R.id.action_bar_check)
        deSelectall = menu.findItem(R.id.action_bar_uncheck)
        menuitem_add = menu.findItem(R.id.action_bar_plus)
        menuitem_add!!.setVisible(!presenter!!.isAdmin())
        return true
    }

    override fun onPrepareOptionsMenu(menu: Menu): Boolean = true

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        try {
            if (item.itemId == android.R.id.home) {
                finish()
            } else if (item.itemId == R.id.attendance_tab) {
                startActivity<AttendenceSelectionListingActivity>(AttendenceSelectionListingActivity.CLASSID_KEY to classid!!)
            } else if (item.itemId == R.id.action_bar_check) {
                selectAll(false)
                deSelectall!!.isVisible = true
                select_all!!.isVisible = false
            } else if (item.itemId == R.id.action_bar_uncheck) {
                selectAll(true)
                select_all!!.isVisible = true
                deSelectall!!.isVisible = false
            } else if (item.itemId == android.R.id.home) {
                finish()
            } else if (item.itemId == R.id.action_bar_plus) {
                startActivityForResult<StudentDetailEditPage>(2, StudentDetailEditPage.CLASS_ID_KEY to classid!!)
            } else if (item.itemId == R.id.action_sort_name) {

                sortStudentsByName()

            } else if (item.itemId == R.id.action_roll_number) {

                sortStudentsByNumber()

            }

        } catch (e: Exception) {
            e.printStackTrace()
        }
        return true
    }


    private fun sortStudentsByName() {
        val list = ArrayList<MyStudentModel>()
        adapter!!.items.forEachWithIndex { i, displayItem ->
            list.add(i, displayItem as MyStudentModel)
        }
        Collections.sort(list, Sortbyname())
        adapter!!.addItems(true, list)
    }

    private fun sortStudentsByNumber() {
        val list = ArrayList<MyStudentModel>()
        adapter!!.items.forEachWithIndex { i, displayItem ->
            list.add(i, displayItem as MyStudentModel)
        }
        Collections.sort(list, Sortbyroll())
        adapter!!.addItems(true, list)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == 2) {

            showLoading()
            presenter?.refreshdata(classid!!)
        }
    }


}