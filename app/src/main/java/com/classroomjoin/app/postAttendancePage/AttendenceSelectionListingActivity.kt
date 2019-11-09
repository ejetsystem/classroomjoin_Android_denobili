package com.classroomjoin.app.postAttendancePage


import android.app.ProgressDialog
import android.graphics.Color
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.design.widget.TabLayout
import android.support.v7.app.AlertDialog
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.classroomjoin.app.R
import com.classroomjoin.app.helper.DialogUtil
import com.classroomjoin.app.helper.ItemClickSupport
import com.classroomjoin.app.helper_utils.*
import com.classroomjoin.app.loginPage.LoginModel
import com.classroomjoin.app.loginPage.loiginMicroService.LoginMicroEvent
import com.classroomjoin.app.loginPage.loiginMicroService.LoginMicroPresenter
import com.classroomjoin.app.loginPage.signupGoogle.SignInGoogleModel
import com.classroomjoin.app.mystudentsPage.MyStudentModel
import com.classroomjoin.app.mystudentsPage.Sortbyname
import com.classroomjoin.app.mystudentsPage.Sortbyroll
import com.classroomjoin.app.postAttendancePage.checkAttendance.AttendanceMicroEvent
import com.classroomjoin.app.postAttendancePage.checkAttendance.AttendanceMicroModel
import com.classroomjoin.app.postAttendancePage.checkAttendance.AttendanceMicroPresenter
import com.classroomjoin.app.signUpPage.SignUpActivity
import com.classroomjoin.app.teacherOutboxPage.Outboxpage
import com.classroomjoin.app.templatePage.Template
import com.classroomjoin.app.templatePage.TemplateSelectionEvent
import com.classroomjoin.app.templatePage.TemplateSelectionInteractor
import com.classroomjoin.app.templatePage.TemplateSelectionPresenter
import com.vicpin.krealmextensions.query
import io.realm.RealmQuery
import kotlinx.android.synthetic.main.activity_student_selection.*
import kotlinx.android.synthetic.main.login_page.*
import org.jetbrains.anko.*
import org.jetbrains.anko.collections.forEachWithIndex
import rx.Observer
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class AttendenceSelectionListingActivity : BaseListActivity(), AnkoLogger {

    private var adapter: AttendenceSelectionAdapter? = null
    private var presenter: AttendenceSelectionPresenter? = null
    private var progressdialog: ProgressDialog? = null
    private var classid: String? = "0"
    private var item: MenuItem? = null
    private var account_id: Int? = -1

    private var attendanceMicroPresenter: AttendanceMicroPresenter? = null

    private var templatePresenter: TemplateSelectionPresenter? = null


    var lists: List<Template>?=null

    companion object AttendenceClass {
        var CLASSID_KEY: String = "com.classroom.attendance"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_student_selection)
        initviews()

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        attendace_list_tabview.addTab(attendace_list_tabview.newTab().setText(getString(R.string.list_sheet)))
        attendace_list_tabview.addTab(attendace_list_tabview.newTab().setText(getString(R.string.bubble_sheet)))

        attendace_list_tabview.addOnTabSelectedListener(listener)
        attendace_list_tabview.setTabTextColors(Color.parseColor("#BDBDBD"), Color.parseColor("#ffffff"))

        adapter = AttendenceSelectionAdapter(null)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this@AttendenceSelectionListingActivity)
        recyclerView.isNestedScrollingEnabled = false


        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_arrow_back_green_24dp)
        supportActionBar?.setTitle(getString(R.string.title_attendence))

        MainBus.getInstance().busObservable.ofType(AttendenceSelectionEvent::class.java).subscribe(eventobserver)
        MainBus.getInstance().busObservable.ofType(AttendanceMicroEvent::class.java).subscribe(eventobserver_micro)
        MainBus.getInstance().busObservable.ofType(TemplateSelectionEvent::class.java).subscribe(eventobserver_template)
        presenter = AttendenceSelectionPresenter(this)
        attendanceMicroPresenter = AttendanceMicroPresenter(this)
        templatePresenter = TemplateSelectionPresenter(this)

        progressdialog = DialogUtil.showProgressDialog(this@AttendenceSelectionListingActivity)
        classid = intent.getStringExtra(CLASSID_KEY)
        counter_fab.imageResource = R.drawable.ic_send_white_24dp
        counter_fab.visibility = View.GONE

        counter_fab.onClick {
            if (getSelecteditems().isEmpty() && getDeSelecteditems().isEmpty()) showSnackbar(getString(R.string.please_select_present_students))
            else sendAttendence()
        }

        ItemClickSupport.addTo(recyclerView).setOnItemClickListener { _, position, _ ->
            var student: MyStudentModel = adapter?.getItem(position) as MyStudentModel
            student.isSelected = !student.isSelected!!
            adapter?.notifyItemChanged(position, student)
        }

        attendanceMicroPresenter!!.postdata(AttendanceMicroModel(getCurrentDate11(), classid))


    }

    private fun getCurrentDate11(): String {
        val c = Calendar.getInstance()
        val df = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return df.format(c.getTime())

    }

    private val listener = object : TabLayout.OnTabSelectedListener {
        override fun onTabReselected(tab: TabLayout.Tab?) {
        }

        override fun onTabUnselected(tab: TabLayout.Tab?) {
        }

        override fun onTabSelected(tab: TabLayout.Tab?) {
            if (tab?.position == 0) {
                adapter?.items?.forEachWithIndex { i, displayItem ->
                    var student: MyStudentModel = displayItem as MyStudentModel
                    student.isListView = true
                    student.isSelected = true
                    recyclerView.layoutManager = LinearLayoutManager(this@AttendenceSelectionListingActivity)
                    adapter?.notifyItemChanged(i, student)
                }
                //  empty!!.visibility = View.VISIBLE
                // spinner!!.setSelection(1)
                item!!.setVisible(true)

            } else if (tab?.position == 1) {
                adapter?.items?.forEachWithIndex { i, displayItem ->
                    var student: MyStudentModel = displayItem as MyStudentModel
                    student.isListView = false
                    student.isSelected = true
                    recyclerView.layoutManager = GridLayoutManager(this@AttendenceSelectionListingActivity, 4)
                    adapter?.notifyItemChanged(i, student)
                }
                // spinner!!.visibility = View.GONE
                item!!.setVisible(false)

                sortStudentsByNumber()
            }
        }

    }

    private fun sendAttendence() {

        lists= Template().query { realmQuery: RealmQuery<Template> ->
            realmQuery.beginGroup().equalTo("orgid", account_id).endGroup()
                    .beginGroup()
                    .equalTo("type_id", 4)
                    .or().equalTo("type_id", 5)
                    .endGroup()
        }



        println("Data--->@@sendAttendence-->"+lists!!.size+" acc--->"+account_id)

        var templateDataAbsentSubject:String?=null
        var templateDateAbsentText:String?=null
        var templateDataPresentSubject:String?=null
        var templateDatePresentText:String?=null

        for (template in lists!!) {

            println("Data--->@@template-->"+template.type_id)

            if(template.type_id==4){
                println("Data--->@@template-->"+template.message)

                templateDataPresentSubject="Present"
                templateDatePresentText=template.message
            }else if(template.type_id==5){
                templateDataAbsentSubject="Absent"
                templateDateAbsentText=template.message
            }
        }

        progressdialog!!.show()
        val dmyFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val currentDateTimeString = dmyFormat.format(Date())
        presenter!!.postdata(PostAttendence(currentDateTimeString, getSelecteditems(),
                classid, getDeSelecteditems(), getCurrentDate(),templateDataAbsentSubject,templateDateAbsentText,
                templateDataPresentSubject,templateDatePresentText,"","" ))

    }

    private fun getCurrentDate(): String {
        val c = Calendar.getInstance()
        val df = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        return df.format(c.getTime())
    }

/*
    private fun showerrorDialog(message: String){
        var alert = AlertDialog.Builder(this@AttendenceSelectionListingActivity);
        alert.setTitle("Error")
        alert.setMessage(message)
        alert.setPositiveButton("Retry") {
            dialog, whichButton ->  sendAttendence()
            dialog.dismiss()
        }
        alert.setNegativeButton("Cancel"){
            dialog, whichButton ->  dialog.dismiss()
        }
        alert.show()
    }*/

    private fun showerrorDialog(message: String?) {
        var alert = AlertDialog.Builder(this@AttendenceSelectionListingActivity)
        alert.setTitle(getString(R.string.app_name))
        alert.setMessage(message)
        alert.setPositiveButton(getString(R.string.btn_retry)) { dialog, whichButton ->
            sendAttendence()
            dialog.dismiss()
        }
        alert.setNegativeButton(getString(R.string.btn_cancel)) { dialog, whichButton ->
            dialog.dismiss()
        }
        alert.show()
    }

    private fun getSelecteditems(): List<String> {
        val list: ArrayList<String>? = ArrayList<String>()
        adapter?.items?.forEachWithIndex { i, displayItem ->
            if ((displayItem as MyStudentModel).isSelected!!) {
                list?.add((displayItem).id.toString())
            }
        }
        return list!!
    }

    private fun getDeSelecteditems(): List<String> {
        val list: ArrayList<String>? = ArrayList<String>()
        adapter?.items?.forEachWithIndex { i, displayItem ->
            if (!(displayItem as MyStudentModel).isSelected!!) {
                list?.add((displayItem).id.toString())
            }
        }
        return list!!
    }

    override fun onRefresh() {
        swipeContainer.isRefreshing = false
        super.onRefresh()
    }

    private val eventobserver_micro = object : Observer<AttendanceMicroEvent> {
        override fun onCompleted() {}

        override fun onError(e: Throwable) {
            e.printStackTrace()
        }

        override fun onNext(event: AttendanceMicroEvent) {
            if (progressdialog!!.isShowing) progressdialog!!.dismiss()
            when (event.event) {
                Event.ERROR ->{showSuccessDialog_SingIn(event.error)}
                //  Event.SERVER_ERROR -> showErrorDialog(getString(R.string.serverError))
                // Event.POST_FAILURE -> showErrorDialog(getString(R.string.serverError))
                Event.POST_SUCCESS -> {

                    println("Data11--->onNext" + "ERROR--->" + event.error)
                    account_id=event.error!!.toInt()
                    templatePresenter!!.getdata(5)

                }
                else -> {
                }
            }

        }

    }

    private val eventobserver_template = object : Observer<TemplateSelectionEvent> {
        override fun onCompleted() {}

        override fun onError(e: Throwable) {
            e.printStackTrace()
        }

        override fun onNext(event: TemplateSelectionEvent) {
            when (event.event) {
                Event.RESULT -> {
                    // adapter!!.addItems(true, event.results)
                    // showResults()
                   // println("Data--->@@eventobserver_template-->onNext")

                     /*lists= Template().query { realmQuery: RealmQuery<Template> ->
                        realmQuery.beginGroup().equalTo("account_id", 1).endGroup()
                                .beginGroup()
                                .equalTo("type_id", 4)
                                .or().equalTo("type_id", 5)
                                .endGroup()

                         println("Data--->@@eventobserver_template-->"+lists!!.size)
*/
                     //}
                }
                    Event.NO_RESULT ->{}
                    // showNoResults(getString(R.string.no_templates_added))
                    Event.NO_INTERNET -> {

                    }
                    Event.SERVER_ERROR -> {}
                    Event.ERROR -> {}
                    else -> {
                    }
                }
            }
        }

        private fun showSuccessDialog_SingIn(message: String?) {

            if (progressdialog!!.isShowing)
                progressdialog!!.dismiss()

            var alert = android.app.AlertDialog.Builder(this)
            alert.setTitle(R.string.app_name)
            alert.setMessage("You have already taken attendance for this class")
            alert.setCancelable(false)
            alert.setNeutralButton(R.string.btn_ok) { dialog, whichButton ->
                dialog.dismiss()
                finish()

            }

            alert.show()
        }

        private val eventobserver = object : Observer<AttendenceSelectionEvent> {
            override fun onCompleted() {}

            override fun onError(e: Throwable) {
                e.printStackTrace()
            }

            override fun onNext(event: AttendenceSelectionEvent) {
                if (swipeContainer.isRefreshing) swipeContainer.isRefreshing = false
                if (progressdialog!!.isShowing) progressdialog?.dismiss()
                when (event.event) {
                    Event.RESULT -> {
                        attendace_list_tabview.visibility = View.VISIBLE
                        counter_fab.visibility = View.VISIBLE
                        var list = ArrayList<DisplayItem>()
                        var listview = true
                        if (attendace_list_tabview.selectedTabPosition == 1) listview = false
                        event.results!!.forEachWithIndex { i, displayItem ->
                            var student: MyStudentModel = displayItem as MyStudentModel
                            student.isListView = listview
                            student.isSelected = true
                            list.add(student)
                        }
                        adapter!!.addItems(true, list)
                        showResults()
                    }
                    Event.NO_RESULT -> {
                        if (adapter!!.itemCount == 0)
                            showNoResults(getString(R.string.no_attendance_added))
                    }
                    Event.NO_INTERNET -> {
                        if (adapter!!.itemCount == 0)
                            showNoInternet()
                    }
                    Event.SERVER_ERROR -> if (adapter!!.itemCount == 0) showServerError()
                    Event.ERROR -> if (adapter!!.itemCount == 0) showError(event.error)
                    Event.POST_FAILURE -> showerrorDialog(event.error)
                    Event.POST_SUCCESS -> showSuccessDialog()
                    Event.SAVED_TO_OUTBOX -> {
                        Snackbar.make(recyclerView, R.string.saved_to_outbox, Snackbar.LENGTH_LONG)
                                .setAction(R.string.go_to_outbox, snack_listener)
                                .show()
                    }
                    else -> {
                    }
                }
            }
        }

        private val snack_listener = object : View.OnClickListener {
            override fun onClick(p0: View?) {
                startActivity<Outboxpage>()
            }

        }


        override fun onCreateOptionsMenu(menu: Menu?): Boolean {
            menuInflater.inflate(R.menu.options_menu_home, menu)

            item = menu!!.findItem(R.id.empty)
            /* val item = menu!!.findItem(R.id.spinner)
             spinner = MenuItemCompat.getActionView(item) as Spinner

             val adapter = ArrayAdapter.createFromResource(this,
                     R.array.Sort, android.R.layout.simple_spinner_item)
             adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
             spinner!!.adapter = adapter
             spinner!!.onItemSelectedListener { onItemSelected { adapterView, view, i, l -> if (i == 0) sortStudentsByName() else sortStudentsByNumber() } }
           */  return true
        }

        override fun onPrepareOptionsMenu(menu: Menu): Boolean = true

        override fun onOptionsItemSelected(item: MenuItem): Boolean {
            try {
                if (item.itemId == android.R.id.home) {
                    finish()
                } else if (item.itemId == R.id.action_sort_name_att) {

                    sortStudentsByName()

                } else if (item.itemId == R.id.action_roll_number_att) {

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


        private fun showSuccessDialog() {
            alert(getString(R.string.successfully_posted_attendance)) {
                title(getString(R.string.app_name))
                yesButton {
                    dialog?.dismiss()
                    getData()
                }
            }.show()
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
            getData()
        }


        private fun getData() {
            showLoading()
            presenter!!.getdata(classid!!)
        }

    }
