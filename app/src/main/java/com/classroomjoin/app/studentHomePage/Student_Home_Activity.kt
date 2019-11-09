package com.classroomjoin.app.studentHomePage

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
import com.classroomjoin.app.R
import com.classroomjoin.app.attendancePage.ParentAttendanceActivity
import com.classroomjoin.app.homepage.HomeViewModel
import com.classroomjoin.app.loginPage.LoginActivity
import com.classroomjoin.app.manageAccountsPackage.ManageAccountsPage
import com.classroomjoin.app.myConnectedStudents.MyConnectedStudentsListingActivity
import com.classroomjoin.app.profilePage.ProfilePage
import com.classroomjoin.app.splashPage.LanguageSelectionActivity
import com.classroomjoin.app.studentDiaryPage.StudentDiaryActivity
import com.classroomjoin.app.studentInboxPage.StudentInboxPage
import com.classroomjoin.app.studentSocialGradeReportPage.StudentSocialGradeReportActivity
import com.classroomjoin.app.studentallEventsPage.AlleventsActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import kotlinx.android.synthetic.main.content_student_home_page.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.onClick
import org.jetbrains.anko.startActivity


class Student_Home_Activity : LocalizationActivity(), NavigationView.OnNavigationItemSelectedListener, AnkoLogger {

    var newTextis: CharSequence? = null
    private var progressdialog: ProgressDialog? = null
    private var eventid: Int = 0
    private val USERNAME_KEY = "com.classroom.setting.username"
    private val USER_EMAIL_KEY = "com.classroom.setting.useremail"
    private val STUDENT_ID = "com.classroom.setting.student_id"
    private val StUDENT_NAME = "com.classroom.setting.student_name"
    private val viewmodel: HomeViewModel by lazy {
        HomeViewModel(this@Student_Home_Activity)
    }


    private val CLASS_NAME_KEY = "com.classroom.setting.classname"
    private var avatar_view: AvatarView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_student__home_)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(applicationContext)

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


    override fun onResume() {
        super.onResume()
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(applicationContext)
        val navigationView = findViewById<NavigationView>(R.id.nav_view)
        navigationView.setNavigationItemSelectedListener(this)
        val headerlayout: View = navigationView.getHeaderView(0)
        val username: TextView = headerlayout.findViewById<TextView>(R.id.username_field)
        val usermail: TextView = headerlayout.findViewById<TextView>(R.id.user_mail_id)
        avatar_view = headerlayout.findViewById<AvatarView>(R.id.profile_image_new)

        username.text = sharedPreferences.getString(USERNAME_KEY, "Username")
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
            startActivity(Intent(this@Student_Home_Activity, LoginActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
            finish()

        }

        alert.setNegativeButton(R.string.btn_no) { dialog, whichButton ->
            dialog.dismiss()
            // finish()
        }
        alert.show()
    }
}
