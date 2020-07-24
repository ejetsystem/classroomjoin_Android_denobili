package com.denobili.app.emailSettingPage

import android.content.Context
import com.denobili.app.R
import com.denobili.app.helper.RxFunctions
import com.denobili.app.helper_utils.ApiInterface
import rx.Observable
import rx.Observer
import java.text.SimpleDateFormat
import java.util.*


class CommunicationSettingManager {

    var context11:Context?=null

    fun getdata(apiInterface: ApiInterface, accesstoken: String, org_id: String, id: String, listener: CommunicationSettingListener,context: Context) {
        context11=context
        apiInterface.getComSettings(accesstoken, org_id, id).compose(RxFunctions.applySchedulers()).subscribe(detailsObserver(listener))
    }


    private fun detailsObserver(listener: CommunicationSettingListener): Observer<CommunicationSettingResponse> {
        return object : Observer<CommunicationSettingResponse> {

            override fun onNext(t: CommunicationSettingResponse?) {
                if (t?.status == "Success") {
                    listener.onCommunicationSettingFetched(t.data)
                } else listener.onCommunicationSettingFetchingError(t!!.error_message)
            }

            override fun onError(e: Throwable?) {
                listener.onCommunicationSettingFetchingError(context11!!.getString(R.string.serverError))
                e?.printStackTrace()
            }

            override fun onCompleted() {
            }
        }
    }

    fun postData(model: CommunicationSettingModel, accesstoken: String, apiInterface: ApiInterface, listener: CommunicationSettingListener,context: Context) {
        context11=context
        if (model.comm_id != null) apiInterface.putCommunicationSetting(accesstoken, model.comm_id!!, model.username, model.password, model.senderid, model.orgid, getCurrentDate(),
                model.attendanceSms,
                model.smsMain,
                model.emailAttendance,
                model.emailMain,
                model.messageAttendancePresent,
                model.messageAttendanceAbsent,
                model.messageMain).compose(RxFunctions.applySchedulers())
                .subscribe(setObserver(listener))
        else apiInterface.addCommunicationSetting(accesstoken, model).compose(RxFunctions.applySchedulers()).subscribe(setObserver(listener))
    }


    private fun setObserver(listener: CommunicationSettingListener): Observer<CommunicationSettingAddResponse> {
        return object : Observer<CommunicationSettingAddResponse> {
            override fun onCompleted() {
            }

            override fun onError(e: Throwable?) {
                listener.onCommunicationSettingFailed(context11!!.getString(R.string.serverError))
                e?.printStackTrace()
            }

            override fun onNext(t: CommunicationSettingAddResponse?) {
                if (t?.status == "Success") {
                    listener.onCommunicationSettingSuccess()
                } else listener.onCommunicationSettingFailed(t!!.error_message)

            }
        }
    }


    fun deactivate(id: Int, apiInterface: ApiInterface, accesstoken: String): Observable<CommunicationSettingDeactivate> =
            apiInterface.setCommunicationSetting(accesstoken, id.toString(), 0,getCurrentDate()).compose(RxFunctions.applySchedulers())

    private fun getCurrentDate(): String {
        val c = Calendar.getInstance()
        val df = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        return df.format(c.time)
    }


}