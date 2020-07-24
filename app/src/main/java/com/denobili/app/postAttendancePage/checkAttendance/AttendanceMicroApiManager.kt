package com.denobili.app.postAttendancePage.checkAttendance

import android.content.Context
import com.denobili.app.R
import com.denobili.app.helper.RxFunctions
import com.denobili.app.helper_utils.ApiInterface
import retrofit2.adapter.rxjava.Result
import rx.Observer


class AttendanceMicroApiManager {

    private var context: Context? = null
    private var account_id: Int? = null

    fun postdata(accesstoken:String,otPverifyModel: AttendanceMicroModel, apiInterface: ApiInterface,
                 listener: AttendanceMicroRequestListener, context11: Context?, account: Int) {
        context = context11
        account_id=account
        apiInterface.checkAttendance(accesstoken,otPverifyModel).compose(RxFunctions.applySchedulers()).subscribe(observer(listener))

    }

    fun observer(listener: AttendanceMicroRequestListener): Observer<Result<AttendanceMicroApiResponse>> {
        return object : Observer<Result<AttendanceMicroApiResponse>> {
            override fun onCompleted() {

            }

            override fun onNext(map: Result<AttendanceMicroApiResponse>) {

                if (map?.response()!!.isSuccessful) {
                    val attendanceMicroApiResponse: AttendanceMicroApiResponse? = map.response().body()

                    println("Data11--->onNext" + "onError--->" + attendanceMicroApiResponse!!.data)


                   if(attendanceMicroApiResponse!!.data!!)
                       listener.onError(account_id.toString())
                    else
                       listener.onRequestSuccesful(account_id.toString())
                }

            }

            override fun onError(e: Throwable?) {
                // println("Data11--->onNext" + "onError--->"+e.toString())

                listener.onRequestError(context!!.getString(R.string.serverError))
            }

        }
    }

    interface AttendanceMicroRequestListener {

        fun onRequestSuccesful(error: String)

        fun onRequestError(error: String)

        fun onError(error: String)


    }

}