package com.denobili.app.studentHomePage

import agency.tango.android.avatarview.loader.PicassoLoader
import agency.tango.android.avatarview.views.AvatarView
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import com.akexorcist.localizationactivity.ui.LocalizationActivity
import com.denobili.app.R
import com.denobili.app.attendancePage.ParentAttendanceActivity
import com.denobili.app.helper_utils.*
import com.denobili.app.homepage.HomeViewModel
import com.denobili.app.invoice.InvoiceListingActivity
import com.denobili.app.landing_page.LandingActivity
import com.denobili.app.manageAccountsPackage.ManageAccountsPage
import com.denobili.app.myConnectedStudents.MyConnectedStudentsListingActivity
import com.denobili.app.profilePage.ProfilePage
import com.denobili.app.readStatus.ReadStatusModel
import com.denobili.app.splashPage.LanguageSelectionActivity
import com.denobili.app.studentDiaryPage.StudentDiaryActivity
import com.denobili.app.studentEventDetailPage.EventDetailFetch
import com.denobili.app.studentEventDetailPage.EventDetailPresenter
import com.denobili.app.studentInboxPage.StudentInboxPage
import com.denobili.app.studentSocialGradeReportPage.StudentSocialGradeReportActivity
import com.denobili.app.studentallEventsPage.AlleventsActivity
import com.denobili.app.studentallEventsPage.StudentEventModel
import com.denobili.app.utils.SharedPreferencesData
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import kotlinx.android.synthetic.main.activity_event_detail_new.*
import kotlinx.android.synthetic.main.content_student_home_page.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.onClick
import org.jetbrains.anko.startActivity
import rx.Observer


class Student_Home_Activity : LocalizationActivity(), NavigationView.OnNavigationItemSelectedListener, AnkoLogger {
    var sharedPreferences: SharedPreferencesData?=null
    var newTextis: CharSequence? = null
    private var progressdialog: ProgressDialog? = null
    private var eventid: Int = 0
    private var presenter: BadgePresenter?=null
    var userId:String?=null
    var orgId:String?=null
    private val USERNAME_KEY = "com.classroom.setting.username"
    private val USER_EMAIL_KEY = "com.classroom.setting.useremail"
    private val STUDENT_ID = "com.classroom.setting.student_id"
    private val StUDENT_NAME = "com.classroom.setting.student_name"
    private val USER_ID="com.classroom.account_id"
    private var ORG_ID="com.classroom.org_id"
    private val viewmodel: HomeViewModel by lazy {
        HomeViewModel(this@Student_Home_Activity)
    }


    private val CLASS_NAME_KEY = "com.classroom.setting.classname"
    private var avatar_view: AvatarView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_student__home_)

        sharedPreferences=SharedPreferencesData(this)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        MainBus.getInstance().busObservable.ofType(BadgeFetch::class.java).subscribe(eventobserver)
        presenter= BadgePresenter(this@Student_Home_Activity)
        presenter?.getdata()

      /* val diary= sharedPreferences!!.DiaryCount
        val message= sharedPreferences!!.messageCount
        val assign= sharedPreferences!!.assignCount
        setSupportActionBar(toolbar)
        if (assign==null||assign!!.equals("")){
            assignCount.visibility=View.GONE
        }else{
            assignCount.setText(assign)

        }
        if (message==null||message!!.equals("")){
            messageCount.visibility=View.GONE
        }else{
            messageCount.setText(message)

        }
        if (diary==null||diary!!.equals("")){
            diaryCount.visibility=View.GONE
        }else{
            diaryCount.setText(diary)

        }*/

        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(applicationContext)
        orgId=sharedPreferences.getString(ORG_ID, "0")
        if (orgId=="51"||orgId=="57"||orgId=="58")
        {
            feeCard.visibility=View.GONE

        }else{

        }

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        //supportActionBar?.setLogo(R.drawable.ic_icon_logo)
        supportActionBar?.setDisplayUseLogoEnabled(true)
        supportActionBar?.setTitle(getString(R.string.home))

        home_all_events.onClick { startActivity<AlleventsActivity>() }

        home_assignment_events.onClick {
            startActivity(intentFor<StudentDiaryActivity>(StudentDiaryActivity.TEMPLATE_NAME_KEY to getString(R.string.assignement),
                    StudentDiaryActivity.TEMPLATE_TYPE_KEY to "2"))
        }

        home_diary_events.onClick {
            startActivity(intentFor<StudentDiaryActivity>(StudentDiaryActivity.TEMPLATE_NAME_KEY to getString(R.string.diary),
                    StudentDiaryActivity.TEMPLATE_TYPE_KEY to "1"))
        }

        home_notice_events.onClick {

            startActivity(intentFor<StudentDiaryActivity>(StudentDiaryActivity.TEMPLATE_NAME_KEY to getString(R.string.notice),
                    StudentDiaryActivity.TEMPLATE_TYPE_KEY to "3"))
        }
        home_event_events.onClick {
            startActivity(intentFor<StudentDiaryActivity>(StudentDiaryActivity.TEMPLATE_NAME_KEY to getString(R.string.events),
                    StudentDiaryActivity.TEMPLATE_TYPE_KEY to "4"))
        }
        invoice_lay.onClick {
            userId=sharedPreferences.getString(USER_ID, "0")
            val invoice=Intent(this,InvoiceListingActivity::class.java)
           invoice.putExtra("userId",userId)
            startActivity(invoice)
        }
        home_social_grade_events.onClick { startActivity<StudentSocialGradeReportActivity>() }

       // home_inbox_events.onClick { startActivity<StudentInboxPage>() }

        home_inbox_events.onClick {
            startActivity(intentFor<StudentInboxPage>(StudentInboxPage.TEMPLATE_NAME_KEY to getString(R.string.inbox),
                    StudentInboxPage.TEMPLATE_TYPE_KEY to "3"))
        }


        sms_layout.onClick {
            startActivity(intentFor<StudentInboxPage>(StudentInboxPage.TEMPLATE_NAME_KEY to getString(R.string.gallery),
                    StudentInboxPage.TEMPLATE_TYPE_KEY to "6"))
        }

       // sms_layout.onClick { startActivity<StudentInboxPage>() }


        home_attendance_events.onClick {
            startActivity(intentFor<ParentAttendanceActivity>(ParentAttendanceActivity.studentidkey to viewmodel.student_id))
        }

        val drawer = findViewById<DrawerLayout>(R.id.drawer_layout)
        val toggle = ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer.setDrawerListener(toggle)
        toggle.syncState()


        val classname = sharedPreferences.getString(CLASS_NAME_KEY, "classname")
        row_connected_studentname_short.text = sharedPreferences.getString(StUDENT_NAME, "Name")
        System.out.println("Data--->" + "profile_url--->" + viewmodel.profile_url)

        // PicassoLoader().loadImage(row_connected_studentname_short!!,viewmodel.profile_url,viewmodel.user_name)


        row_connected_student_name.text = sharedPreferences.getString(StUDENT_NAME, "Name")
        home_student_id.text = classname


        // getDateFormat("")

    }
    private val eventobserver = object : Observer<BadgeFetch> {
        override fun onCompleted() {}

        override fun onError(e: Throwable) {
            e.printStackTrace()
        }

        override fun onNext(event: BadgeFetch) {
            //if(progressdialog!!.isShowing)progressdialog!!.hide()
            when (event.event) {
                Event.SERVER_ERROR ->shownoresults(event.message)
                Event.RESULT->{
                    invalidateOptionsMenu()
                    showresults(event.studentDetail)

                    //read_status_presenter?.postdata(ReadStatusModel(eventid.toString(),type_id,"1",getCurrentDate()))

                }
                Event.NO_RESULT->shownoresults(event.message)
                Event.NO_INTERNET->shownoresults(getString(R.string.noInternet))
                else ->{
                }
            }
        }
    }
    private fun shownoresults(error: String){

    }
    private fun showresults(studentDetail: BadgeCountModel?) {
        if (studentDetail!!.allUnreadNotification.equals("0"))
            all_count.visibility=View.GONE
        else
        all_count.text=studentDetail!!.allUnreadNotification
        if (studentDetail!!.diaryUnread.equals("0"))
            diary_count.visibility=View.GONE
        else
        diary_count.text=studentDetail!!.diaryUnread
        if (studentDetail!!.diaryEventUnread.equals("0"))
            event_count.visibility=View.GONE
        else
        event_count.text=studentDetail.diaryEventUnread
        if (studentDetail!!.smsUnread.equals("0"))
            sms_count.visibility=View.GONE
        else
        sms_count.text=studentDetail.smsUnread
        if (studentDetail!!.diaryAssignmentUnread.equals("0"))
            assign_count.visibility=View.GONE
        else
        assign_count.text=studentDetail.diaryAssignmentUnread
        if (studentDetail!!.socialgradeUnread.equals("0"))
            grade_count.visibility=View.GONE
        else
        grade_count.text=studentDetail.socialgradeUnread
        if (studentDetail!!.messageUnread.equals("0"))
            inbox_count.visibility=View.GONE
        else
        inbox_count.text=studentDetail.messageUnread
    }
    private fun getData() {

    }


    override fun onResume() {
        super.onResume()
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(applicationContext)
        presenter!!.getdata()
        val navigationView = findViewById<NavigationView>(R.id.nav_view)
        navigationView.setNavigationItemSelectedListener(this)
        val headerlayout: View = navigationView.getHeaderView(0)
        val username: TextView = headerlayout.findViewById<TextView>(R.id.username_field)
        val usermail: TextView = headerlayout.findViewById<TextView>(R.id.user_mail_id)
        avatar_view = headerlayout.findViewById<AvatarView>(R.id.profile_image_new)

        username.text = sharedPreferences.getString(USERNAME_KEY, "Username")
        //usermail.text = sharedPreferences.getString(USER_EMAIL_KEY, "Mail id")
        val string=sharedPreferences.getString(USER_EMAIL_KEY, "Mail id")
        val index = string.indexOf('@')
        val domain: String? = if (index == -1) null else string.substring(index + 1)
        if (domain!!.equals("app.classroomjoin.com"))
            usermail.text=""
        else
            usermail.text = sharedPreferences.getString(USER_EMAIL_KEY, "Mail id")
        PicassoLoader().loadImage(avatar_view!!, viewmodel.profile_url, viewmodel.user_name)
    }

    override fun onBackPressed() {
        val drawer = findViewById<DrawerLayout>(R.id.drawer_layout)
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        return false
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        return super.onOptionsItemSelected(item)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        val id = item.itemId
        if (id == R.id.nav_logout) {

            showSuccessDialog1()

        } else if (id == R.id.nav_all_events) {
            startActivity<AlleventsActivity>()
        } else if (id == R.id.nav_message_events) {
            startActivity<StudentInboxPage>()
        } else if (id == R.id.nav_social_grade_event) {
            startActivity<StudentSocialGradeReportActivity>()
        } else if (id == R.id.nav_diary_events) {
            startActivity(intentFor<StudentDiaryActivity>(StudentDiaryActivity.TEMPLATE_NAME_KEY to getString(R.string.diary),
                    StudentDiaryActivity.TEMPLATE_TYPE_KEY to "1"))
        } else if (id == R.id.nav_connected_students) {
            startActivity<MyConnectedStudentsListingActivity>()
        } else if (id == R.id.nav_accounts) {
            startActivity<ManageAccountsPage>()
        } else if (id == R.id.nav_profile) {
            startActivity<ProfilePage>()
        } else if (id == R.id.nav_language_settings_parent) {
            startActivity(intentFor<LanguageSelectionActivity>("is_from" to false))
        }

        val drawer = findViewById<DrawerLayout>(R.id.drawer_layout)
        drawer.closeDrawer(GravityCompat.START)
        return true
    }

    private fun showSuccessDialog1() {


        var alert = android.app.AlertDialog.Builder(this)
        alert.setTitle(R.string.app_name)
        alert.setMessage(getString(R.string.logout_alert))
        alert.setPositiveButton(R.string.btn_yes) { dialog, whichButton ->
            dialog.dismiss()
            // finish()

            var gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestEmail()
                    .requestProfile()
                    .build()

            // Build a GoogleSignInClient with the options specified by gso.
            var mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

            //val acct = GoogleSignIn.getLastSignedInAccount(this)

            val googleSignInAccount = GoogleSignIn.getLastSignedInAccount(this)
            if (googleSignInAccount != null) {
                mGoogleSignInClient.signOut()
            }

            val settings = applicationContext.getSharedPreferences(applicationContext.getString(R.string.PREFERENCE_FILE), Context.MODE_PRIVATE)
            settings.edit().clear().apply()

            viewmodel!!.clearData()
            PreferenceManager.getDefaultSharedPreferences(applicationContext).edit().clear().apply()
            startActivity(Intent(this@Student_Home_Activity, LandingActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
            finish()

        }

        alert.setNegativeButton(R.string.btn_no) { dialog, whichButton ->
            dialog.dismiss()
            // finish()
        }
        alert.show()
    }
}
