package com.classroomjoin.app.readStatus

import android.content.Context
import com.classroomjoin.app.R
import com.classroomjoin.app.helper.RxFunctions
import com.classroomjoin.app.helper_utils.ApiInterface
import rx.Observer


class ReadStatusApiManager {

    private var context: Context? = null

    fun postdata(accesstoken11: String, otPverifyModel: ReadStatusModel, apiInterface: ApiInterface, listener: ReadStatusRequestListener, context11: Context?) {
        context = context11
        apiInterface.readStatus(accesstoken11, otPverifyModel).compose(RxFunctions.applySchedulers()).subscribe(observer(listener))

    }

    fun observer(listener: ReadStatusRequestListener): Observer<ReadStatusReponser> {
        return object : Observer<ReadStatusReponser> {
            override fun onCompleted() {

            }

            override fun onNext(map: ReadStatusReponser) {


            }

            override fun onError(e: Throwable?) {
                // println("Data11--->onNext" + "onError--->"+e.toString())

                listener.onRequestError(context!!.getString(R.string.serverError))
            }

        }
    }

    interface ReadStatusRequestListener {

        fun onRequestSuccesful()

        fun onRequestError(error: String)

        fun onError(error: String)


    }

}