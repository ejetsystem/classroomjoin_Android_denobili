package com.classroomjoin.app.helper_utils

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.preference.PreferenceManager
import com.classroomjoin.app.outboxExpiredPage.OutboxExpiredPage
import com.classroomjoin.app.teacherOutboxPage.OutboxModel
import com.vicpin.krealmextensions.query
import io.realm.Sort
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.error
import java.util.*
import java.util.concurrent.TimeUnit

open class CheckOutboxService : Service(),AnkoLogger {

    override fun onBind(intent: Intent): IBinder? {
        throw UnsupportedOperationException("Not yet implemented")
    }


    override fun onCreate() {
        super.onCreate()
        checkExpiredMessages(getData(PreferenceManager.getDefaultSharedPreferences(applicationContext).getString(ACCOUNT_ID_KEY,null)))
    }

    fun getData(account_id:String): List<OutboxModel> =
            OutboxModel().query { query -> query.equalTo("account_id",account_id).findAllSorted("gen_date",Sort.ASCENDING)}

    companion object {
        private val ACCESSTOKENKEY = "com.classroom.accesstoken"
        private val USERIDKEY = "com.classroom.account_id"
        private val ORGID_KEY = "com.classroom.org_id"
        private val ACCOUNT_ID_KEY = "com.classroom.account.id"
        private val TEACHER_ID_KEY = "com.classroon.teacher.id"
        private val USERTYPE_KEY = "com.classroom.setting.usertype"
        private val ADMIND_ID_KEY = "com.classroom.setting.admin_id_key"
        private val USERNAME_KEY = "com.classroom.setting.username"
        private val USER_EMAIL_KEY = "com.classroom.setting.useremail"
        private val STUDENT_ID = "com.classroom.setting.student_id"
        private val StUDENT_NAME = "com.classroom.setting.student_name"
        private val PROFILE_URL = "com.classroom.setting.profile_url"
        private val isAdminKey = "com.classroom.setting.isadmin"
    }

    fun checkExpiredMessages(list:List<OutboxModel>){
        error { "checking expired items " }
        val endDate   = Date()
        var count=0
        list.forEachIndexed { index, outboxModel ->
            val  duration:Long  = endDate.getTime() - outboxModel.gen_date.getTime()
            val diffInHours:Long = TimeUnit.MILLISECONDS.toMinutes(duration)
            if(diffInHours>5){
                count+=1
            }
        }

        if(count>0){
            val intent=Intent(this.applicationContext,OutboxExpiredPage::class.java)
            intent.flags=Intent.FLAG_ACTIVITY_NEW_TASK
            this.applicationContext.startActivity(intent)
        }

    }
}
