package com.classroomjoin.app.teacherMessageSendPage

import android.content.Context
import com.classroomjoin.app.R
import com.classroomjoin.app.helper.NetworkHelper
import com.classroomjoin.app.helper_utils.Emit
import com.classroomjoin.app.helper_utils.Event
import com.classroomjoin.app.helper_utils.Presenter
import com.classroomjoin.app.helper_utils.Selected_Feature
import com.classroomjoin.app.teacherHomePage.CommunicationSendModel
import com.classroomjoin.app.teacherOutboxPage.OutboxModel
import com.classroomjoin.app.teacherOutboxPage.StudentIdClass
import com.vicpin.krealmextensions.save
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.error
import rx.Observer
import java.text.SimpleDateFormat
import java.util.*


class TeacherMessageSendPresenter(private val context: Context) : Presenter(), AnkoLogger {


    private val messagingInteractor: MessagingApiService

    init {
        messagingInteractor = MessagingApiService(context)
    }

    fun send(sendModel: CommunicationSendModel, is_primary: Boolean) {
        if (NetworkHelper.isOnline(context)) {
            when (sendModel.feature) {
                Selected_Feature.SMS.`val` -> {

                    if (is_primary)
                        messagingInteractor.postSms(SmsSendModel(null, null, sendModel.message!!, sendModel.class_id, sendModel.list!!, getCurrentDate(), sendModel.scheduleTime)).subscribe(observer())
                    else {
                        messagingInteractor.postAlternate(AlternateSendModel(null, null, sendModel.message!!, sendModel.class_id, sendModel.parent_list!!, getCurrentDate(), sendModel.scheduleTime)).subscribe(observer())
                    }
                }
                Selected_Feature.NOTIFICATION_FEATURE.`val` -> {

                    messagingInteractor.postNotification(NotifySendModel(context.getString(R.string.message_outbox),
                            sendModel.message!!, null, null, sendModel.list!!, sendModel.attachment_id, sendModel.class_id, getCurrentDate(), sendModel.scheduleTime)).subscribe(observer())

                }
                Selected_Feature.EMAIL.`val` -> {

                    messagingInteractor.postMail(EmailSendModel(context.getString(R.string.mail_outbox),
                            sendModel.message!!, null, null, sendModel.list!!, sendModel.attachment_id, sendModel.class_id, getCurrentDate(), sendModel.scheduleTime)).subscribe(observer())
                }

                Selected_Feature.DIARY.`val` -> {
                    messagingInteractor.postDiary(DiarySendModel(context.getString(R.string.diray_outbox),
                            sendModel.message!!,
                            null, null,
                            sendModel.diary_event_id!!,
                            sendModel.list!!,
                            sendModel.attachment_id, sendModel.class_id, getCurrentDate())).subscribe(observer())
                }
            }
        } else {
            var model = OutboxModel(messagingInteractor.account_id, messagingInteractor.realm)
            model.feature = sendModel.feature
            model.message = sendModel.message
            model.class_id = sendModel.class_id
            model.scheduleTime = sendModel.scheduleTime

            sendModel.list?.forEachIndexed { index, i ->
                model.list?.add(index, StudentIdClass(i))
            }
            if (sendModel.diary_event_id != null) model.diary_event_id = sendModel.diary_event_id
            if (sendModel.feature != Selected_Feature.SMS.`val`)
                model.attachment_id = sendModel.attachment_id
            model.save()
            postOutbox()
        }
    }

    private fun getCurrentDate(): String {
        val c = Calendar.getInstance()
        val df = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        return df.format(c.getTime())
    }

    private fun observer(): Observer<SendModelResponse> {
        return object : Observer<SendModelResponse> {
            override fun onCompleted() {
            }

            override fun onError(e: Throwable?) {
                error {
                    "error is =" + e!!.printStackTrace() +
                            "message is" + e.message
                }
                postfailure(context.getString(R.string.some_error))
            }

            override fun onNext(t: SendModelResponse?) {
                if (t!!.status == "Success") postsuccess(t!!.data)
                else postfailure(t.error_message)
            }

        }
    }

    fun postOutbox() {
        Emit(TeacherCommSendEvent(Event.SAVED_TO_OUTBOX))
    }

    fun postsuccess(message: String) {
        Emit(TeacherCommSendEvent(Event.POST_SUCCESS, message))
    }

    fun postfailure(message: String) {
        Emit(TeacherCommSendEvent(Event.POST_FAILURE, message))
    }

}