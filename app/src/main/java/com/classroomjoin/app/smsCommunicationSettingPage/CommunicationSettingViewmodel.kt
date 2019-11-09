package com.classroomjoin.app.smsCommunicationSettingPage

import android.content.Context
import com.classroomjoin.app.emailSettingPage.CommunicationSettingAddResponse
import com.classroomjoin.app.emailSettingPage.CommunicationSettingDeactivate
import com.classroomjoin.app.emailSettingPage.CommunicationSettingModel
import com.classroomjoin.app.emailSettingPage.CommunicationSettingResponse
import com.classroomjoin.app.helper.RxFunctions
import com.classroomjoin.app.helper_utils.Interactor
import rx.Observable
import java.text.SimpleDateFormat
import java.util.*

class CommunicationSettingViewmodel
internal constructor(context: Context)
    : Interactor(context) {

    fun getdata(id: String): Observable<CommunicationSettingResponse> =
            apiInterface.getComSettings(accesstoken, org_id, id).compose(RxFunctions.applySchedulers())


    fun editData(model: CommunicationSettingModel): Observable<CommunicationSettingAddResponse> =
            apiInterface.putCommunicationSetting(accesstoken,
                    model.comm_id!!,
                    model.username,
                    model.password,
                    model.senderid,
                    model.orgid,
                    getCurrentDate(),
                    model.attendanceSms,
                    model.smsMain,
                    model.emailAttendance,
                    model.emailMain,
                    model.messageAttendancePresent,
                    model.messageAttendanceAbsent,
                    model.messageMain).compose(RxFunctions.applySchedulers())


    fun addData(model: CommunicationSettingModel) = apiInterface.addCommunicationSetting(accesstoken, model)
            .compose(RxFunctions.applySchedulers())


    fun activate(id: Int, flag: Int): Observable<CommunicationSettingDeactivate> =
            apiInterface.setCommunicationSetting(accesstoken, id.toString(), flag,getCurrentDate())
                    .compose(RxFunctions.applySchedulers())

    private fun getCurrentDate(): String {
        val c = Calendar.getInstance()
        val df = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        return df.format(c.getTime())
    }

}