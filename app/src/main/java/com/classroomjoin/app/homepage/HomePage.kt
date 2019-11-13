package com.classroomjoin.app.homepage

import agency.tango.android.avatarview.loader.PicassoLoader
import agency.tango.android.avatarview.views.AvatarView
import android.Manifest
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.design.widget.NavigationView
import android.support.v4.app.Fragment
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AlertDialog
import android.support.v7.widget.Toolbar
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import com.akexorcist.localizationactivity.ui.LocalizationActivity
import com.classroomjoin.app.R
import com.classroomjoin.app.emailSettingPage.EmailSettingPage
import com.classroomjoin.app.helper.ForceUpdateChecker
import com.classroomjoin.app.helper_utils.Interactor
import com.classroomjoin.app.loginPage.LoginActivity
import com.classroomjoin.app.manageAccountsPackage.ManageAccountsPage
import com.classroomjoin.app.mySocialGradesPage.MySocialGradesListingActivity
import com.classroomjoin.app.profilePage.ProfilePage
import com.classroomjoin.app.smsCommunicationSettingPage.MessageCommunicationSettingPage
import com.classroomjoin.app.smsCommunicationSettingPage.SmsCommunicationSettingPage
import com.classroomjoin.app.splashPage.LanguageSelectionActivity
import com.classroomjoin.app.studentDetailPage.AddStudentPresenter
import com.classroomjoin.app.teacherAddClasses.ClassListFragment
import com.classroomjoin.app.teacherOutboxPage.OutboxModel
import com.classroomjoin.app.teacherOutboxPage.Outboxpage
import com.classroomjoin.app.teacherReportPage.TeacherReportPage
import com.classroomjoin.app.templatePage.TemplateSelectionPage
import com.crashlytics.android.Crashlytics
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.vicpin.krealmextensions.query
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.startActivity

class HomePage : LocalizationActivity(), NavigationView.OnNavigationItemSelectedListener,
        ForceUpdateChecker.OnUpdateNeededListener {

    var myClass: Fragment? = null
    var classList: Fragment? = null

    private val USERNAME_KEY = "com.classroom.setting.username"
    private val USER_EMAIL_KEY = "com.classroom.setting.useremail"

    private var viewmodel: HomeViewModel? = null
    private var avatar_view: AvatarView? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_page)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)



        ForceUpdateChecker.with(this).onUpdateNeeded(this).check()


        supportActionBar?.setTitle(getString(R.string.home))

        Dexter.withActivity(this)
                .withPermissions(
                        Manifest.permission.CAMERA,

                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                ).withListener(object : MultiplePermissionsListener {
                    override fun onPermissionsChecked(report: MultiplePermissionsReport) {/* ... */
                    }

                    override fun onPermissionRationaleShouldBeShown(permissions: List<PermissionRequest>, token: PermissionToken) {/* ... */
                    }
                }).check()


        val drawer = findViewById<DrawerLayout>(R.id.drawer_layout)
        val toggle = ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer.setDrawerListener(toggle)
        toggle.syncState()

        viewmodel = HomeViewModel(this)

        initializeFragments()

        val ft = supportFragmentManager.beginTransaction()
        ft.add(R.id.fragment_container, classList)
        ft.commit()


    }


    override fun onUpdateNeeded(updateUrl: String) {
        // Initialize a new instance of
        val builder = AlertDialog.Builder(this)

        // Set the alert dialog title
        builder.setTitle(getString(R.string.app_name))

        // Display a message on alert dialog
        builder.setMessage("A new version of classroomJOIN app avaliable in PlayStore, please continue to update.")

        // Set a positive button and its click listener on alert dialog
        builder.setPositiveButton("Continue") { dialog, which ->
            // Do something when user press the positive button
            redirectStore(updateUrl!!);

            // Change the app background color
            // root_layout.setBackgroundColor(Color.RED)
        }


        // Display a negative button on alert dialog
        builder.setNegativeButton("No, thanks") { dialog, which ->
            // finish();
        }


        // Finally, make the alert dialog using builder
        val dialog: AlertDialog = builder.create()

        // Display the alert dialog on app interface
        dialog.show()
    }

    private fun redirectStore(updateUrl: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(updateUrl))
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }


    override fun onResume() {
        super.onResume()

        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(applicationContext)


        val navview = findViewById<NavigationView>(R.id.nav_view_teacher)
        navview.setNavigationItemSelectedListener(this)


        if (viewmodel!!.isAdmin) {
            val nav_Menu = navview.getMenu()
            nav_Menu.findItem(R.id.nav_api_settings).setVisible(false)
            nav_Menu.findItem(R.id.nav_manage_section).setVisible(false)

        }

        val headerlayout: View = navview.getHeaderView(0)

        val username: TextView = headerlayout.findViewById<TextView>(R.id.username_field)
        val usermail: TextView = headerlayout.findViewById<TextView>(R.id.user_mail_id)
        avatar_view = headerlayout.findViewById(R.id.profile_image_new)

        username.text = sharedPreferences.getString(USERNAME_KEY, "Username")
        usermail.text = sharedPreferences.getString(USER_EMAIL_KEY, "Mail id")

        PicassoLoader().loadImage(avatar_view!!, viewmodel!!.profile_url, viewmodel!!.user_name)

    }

    internal fun initializeFragments() {
        classList = ClassListFragment()
        myClass = ClassListFragment()

    }

    override fun onBackPressed() {
        val drawer: DrawerLayout = findViewById(R.id.drawer_layout)
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }


    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        val id = item.itemId
        var fragment: Fragment? = null
        if (id == R.id.nav_social_grade) {
            startActivity<MySocialGradesListingActivity>()
        } else if (id == R.id.nav_home) {
            fragment = classList
            supportActionBar!!.title = getString(R.string.home)
            val ft = supportFragmentManager.beginTransaction()
            ft.replace(R.id.fragment_container, fragment)
            ft.commit()
        } else if (id == R.id.nav_logout) {
            val list = OutboxModel().query { query -> query.equalTo("account_id", viewmodel?.account_id) }
            if (list.isEmpty()) showSuccessDialog1()
            else showerrorDialog(getString(R.string.unsend_outbox_items_logout))
        } else if (id == R.id.nav_template) {
            startActivity(Intent(this@HomePage, TemplateSelectionPage::class.java))
        } else if (id == R.id.nav_email_settings) {
            startActivity(Intent(this@HomePage, EmailSettingPage::class.java))
        } else if (id == R.id.nav_sms_settings) {
            startActivity(Intent(this@HomePage, SmsCommunicationSettingPage::class.java))
        } else if (id == R.id.nav_message_settings) {
            startActivity(Intent(this@HomePage, MessageCommunicationSettingPage::class.java))
        } else if (id == R.id.nav_reports) {
            startActivity<TeacherReportPage>()
        } else if (id == R.id.nav_accounts) {
            startActivity<ManageAccountsPage>()
        } else if (id == R.id.nav_profile) {
            startActivity<ProfilePage>()
        } else if (id == R.id.nav_language_settings) {
            startActivity(intentFor<LanguageSelectionActivity>("is_from" to false))
        } else if (id == R.id.nav_outbox) {
            startActivity<Outboxpage>()
        }

        findViewById<DrawerLayout>(R.id.drawer_layout).closeDrawer(GravityCompat.START)
        return false
    }

    private fun showerrorDialog(message: String) {
        var alert = AlertDialog.Builder(this@HomePage)
        alert.setTitle(getString(R.string.warning))
        alert.setMessage(message)
        alert.setPositiveButton(getString(R.string.btn_proceed)) { dialog, whichButton ->
            logout()
            dialog.dismiss()
        }
        alert.setNegativeButton(getString(R.string.btn_cancel)) { dialog, whichButton ->
            dialog.dismiss()
        }
        alert.show()
    }


    private fun showSuccessDialog1() {


        var alert = android.app.AlertDialog.Builder(this)
        alert.setTitle(R.string.success)
        alert.setMessage(getString(R.string.logout_alert))
        alert.setPositiveButton(R.string.btn_yes) { dialog, whichButton ->
            dialog.dismiss()
            // finish()

            logout()

        }

        alert.setNegativeButton(R.string.btn_no) { dialog, whichButton ->
            dialog.dismiss()
            // finish()
        }
        alert.show()
    }

    fun logout() {

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
        viewmodel!!.clearData()
        PreferenceManager.getDefaultSharedPreferences(applicationContext).edit().clear().apply()

        val settings = applicationContext.getSharedPreferences(applicationContext.getString(R.string.PREFERENCE_FILE), Context.MODE_PRIVATE)
        settings.edit().clear().apply()

        startActivity(Intent(this@HomePage, LoginActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
        finish()
    }
}
