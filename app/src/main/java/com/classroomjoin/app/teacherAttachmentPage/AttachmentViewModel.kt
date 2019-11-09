package com.classroomjoin.app.teacherAttachmentPage

import android.content.Context
import com.classroomjoin.app.helper.RxFunctions
import com.classroomjoin.app.helper_utils.Interactor
import okhttp3.MultipartBody
import org.jetbrains.anko.AnkoLogger
import rx.Observable
import java.text.SimpleDateFormat
import java.util.*


class AttachmentViewModel internal constructor(private var context: Context)
    : Interactor(context), AnkoLogger {

    fun uploadImage(images: MultipartBody.Part): Observable<AttachmentResponse> =
            apiInterface.uploadImage(accesstoken, org_id.toInt(), userid.toInt(), images, getCurrentDate()).compose(RxFunctions.applySchedulers())

    fun uploadAttachment(images: List<MultipartBody.Part>, att_id: Int, event_type: Int): Observable<FileAttachmentResponse> =
            apiInterface.uploadAttachment(accesstoken, org_id.toInt(), userid.toInt(), account_id.toInt(), images, att_id, event_type, getCurrentDate()).compose(RxFunctions.applySchedulers())

    fun getCurrentDate(): String {

        //println("Data--->"+"type-->"+org_id+"---"+account_id)

        val c = Calendar.getInstance()
        val df = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        return df.format(c.getTime())
    }

}
