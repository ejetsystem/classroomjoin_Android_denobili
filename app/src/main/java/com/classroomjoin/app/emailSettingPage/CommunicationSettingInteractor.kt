package com.classroomjoin.app.emailSettingPage

import android.content.Context
import com.classroomjoin.app.helper_utils.Interactor
import rx.Observable


class CommunicationSettingInteractor
internal constructor(var context: Context)
    : Interactor(context) {

    val manager: CommunicationSettingManager = CommunicationSettingManager()

    fun postData(model: CommunicationSettingModel, listener: CommunicationSettingListener) {
        model.orgid = org_id
        model.acc_id = account_id
        manager.postData(model, accesstoken, apiInterface, listener,context)
    }

    fun getdata(id: String, listener: CommunicationSettingListener) {
        manager.getdata(apiInterface, accesstoken, org_id, id, listener,context)
    }

    fun deactivate(id: Int): Observable<CommunicationSettingDeactivate> =
            manager.deactivate(id, apiInterface, accesstoken)

}
