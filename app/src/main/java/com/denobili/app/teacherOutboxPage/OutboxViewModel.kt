package com.denobili.app.teacherOutboxPage

import android.content.Context
import com.denobili.app.R
import com.denobili.app.helper.RxFunctions
import com.denobili.app.helper_utils.Interactor
import com.denobili.app.helper_utils.Selected_Feature
import com.denobili.app.postAttendancePage.PostAttendence
import com.denobili.app.socialGradeSelectionPage.SocialGradeSendModel
import com.denobili.app.teacherMessageSendPage.*
import com.vicpin.krealmextensions.createOrUpdate
import com.vicpin.krealmextensions.delete
import com.vicpin.krealmextensions.query
import com.vicpin.krealmextensions.queryAsObservable
import io.realm.Sort
import org.jetbrains.anko.AnkoLogger
import rx.Observable
import java.text.SimpleDateFormat
import java.util.*


class OutboxViewModel(private var context: Context) : Interactor(context), AnkoLogger {


    private val ATTENDANCE_FEATURE = 6

    private val messagingInteractor: MessagingApiService

    init {
        messagingInteractor = MessagingApiService(context)

    }

    fun getData(): Observable<List<OutboxModel>> {
        return OutboxModel().queryAsObservable { query -> query.equalTo("account_id", account_id).findAllSorted("gen_date", Sort.ASCENDING) }
    }


    fun send(sendModel: OutboxModel): Observable<SendModelResponse> {
        val observable: Observable<SendModelResponse> = when (sendModel.feature) {
            Selected_Feature.SMS.`val` -> {
                val studentlist = ArrayList<Int>()
                sendModel.list!!.forEachIndexed { index, studentIdClass ->
                    studentlist.add(studentIdClass.student_id!!)
                }
                messagingInteractor.postSms(SmsSendModel(null, null, sendModel.message!!, sendModel.class_id, studentlist, getCurrentDate(), sendModel.scheduleTime))
            }
            Selected_Feature.NOTIFICATION_FEATURE.`val` -> {
                val studentlist = ArrayList<Int>()
                sendModel.list!!.forEachIndexed { index, studentIdClass ->
                    studentlist.add(studentIdClass.student_id!!)
                }
                messagingInteractor.postNotification(NotifySendModel(context.getString(R.string.message_outbox),
                        sendModel.message!!, null, null, studentlist, sendModel.attachment_id, sendModel.class_id, getCurrentDate(), sendModel.scheduleTime))

            }
            Selected_Feature.EMAIL.`val` -> {
                val studentlist = ArrayList<Int>()
                sendModel.list!!.forEachIndexed { index, studentIdClass ->
                    studentlist.add(studentIdClass.student_id!!)
                }
                messagingInteractor.postMail(EmailSendModel(context.getString(R.string.mail_outbox),
                        sendModel.message!!, null, null, studentlist, sendModel.attachment_id, sendModel.class_id, getCurrentDate(), sendModel.scheduleTime))
            }
            Selected_Feature.SOCIAL_GRADE.`val` -> {
                val studentlist = ArrayList<String>()
                sendModel.list!!.forEachIndexed { index, studentIdClass ->
                    studentlist.add(studentIdClass.student_id!!.toString())
                }
                val social_id_list = ArrayList<String>()
                sendModel.list_social_ids!!.forEachIndexed { index, socialIdClass ->
                    social_id_list.add(socialIdClass.student_id!!)
                }

                getSendObservable(SocialGradeSendModel(null, null, social_id_list, studentlist, sendModel.class_id, getCurrentDate()))

            }
            Selected_Feature.DIARY.`val` -> {
                val studentlist = ArrayList<Int>()
                sendModel.list!!.forEachIndexed { index, studentIdClass ->
                    studentlist.add(studentIdClass.student_id!!)
                }
                messagingInteractor.postDiary(DiarySendModel(context.getString(R.string.diray_outbox),
                        sendModel.message!!,
                        null, null,
                        sendModel.diary_event_id!!,
                        studentlist, sendModel.attachment_id, sendModel.class_id, getCurrentDate()))

            }
            ATTENDANCE_FEATURE -> {
                val pre_list = ArrayList<String>()
                sendModel.pre_list!!.forEachIndexed { index, studentIdClass ->
                    pre_list.add(studentIdClass.student_id!!.toString())
                }
                val abs_list = ArrayList<String>()
                sendModel.abs_list!!.forEachIndexed { index, studentIdClass ->
                    abs_list.add(studentIdClass.student_id!!.toString())
                }
                var post_attendance = PostAttendence(sendModel.attendance_date, pre_list, sendModel.class_id,
                        abs_list,getCurrentDate(),"","","","","","")
                getPostObservable(post_attendance)
            }
            else -> {
                return Observable.just(SendModelResponse("error", "error", "","something went wrong,Please try again later"))
            }
        }
        return observable
    }

    private fun getCurrentDate(): String {
        val c = Calendar.getInstance()
        val df = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        return df.format(c.getTime())
    }

    private fun getSendObservable(model: SocialGradeSendModel): Observable<SendModelResponse> {
        model.acc_id = account_id
        model.orgid = org_id
        return apiInterface.sendSocialGrades(accesstoken, model).compose(RxFunctions.applySchedulers())
    }

    private fun getPostObservable(postAttendence: PostAttendence): Observable<SendModelResponse> {
        postAttendence.accountmanagementid = account_id
        return apiInterface.postattendence(accesstoken, postAttendence).compose(RxFunctions.applySchedulers())
    }

    fun changeSyncState(model: OutboxModel, i: Int) {
        var model = OutboxModel().query { realmQuery -> realmQuery.equalTo("message_id", model.message_id) }.first()
        model.sync_state = i
        model.createOrUpdate()

    }

    fun deleteItem(model: OutboxModel) {
        OutboxModel().delete { realmQuery -> realmQuery.equalTo("message_id", model.message_id) }
    }


}