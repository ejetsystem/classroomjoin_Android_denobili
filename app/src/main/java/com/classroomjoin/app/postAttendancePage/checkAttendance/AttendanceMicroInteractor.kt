package com.classroomjoin.app.postAttendancePage.checkAttendance

import android.content.Context
import com.classroomjoin.app.helper_utils.Interactor

class AttendanceMicroInteractor
internal constructor(context: Context)
    : Interactor(context) {

    fun postdata(otPverifyModel: AttendanceMicroModel, listener: AttendanceMicroApiManager.AttendanceMicroRequestListener, context: Context?) {
        val manager = AttendanceMicroApiManager()
        manager.postdata(accesstoken,otPverifyModel, apiInterface, listener, context,org_id.toInt())
    }
}