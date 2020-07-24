package com.denobili.app.passwordConfirmation

import android.content.Context
import com.denobili.app.R
import com.denobili.app.helper.RxFunctions
import com.denobili.app.helper_utils.ApiInterface
import com.google.gson.JsonObject
import rx.Observer


class ConfirmPassApiManager {

    var context11:Context?=null

    fun postdata(otPverifyModel: ConfirmPassModel, apiInterface: ApiInterface, listener: ConfirmPassRequestListener,context: Context?) {
        context11=context
        apiInterface.confirmpassword(otPverifyModel).compose(RxFunctions.applySchedulers()).subscribe(observer(listener))
    }

    fun observer(listener: ConfirmPassRequestListener): Observer<JsonObject> {
        return object : Observer<JsonObject> {
            override fun onCompleted() {

            }

            override fun onNext(map: JsonObject) {

                if (map.has("errormessage")) {

                    println("Data11--->onNext--->" + map.get("errormessage").asString)
                    listener.onError(map.get("errormessage").asString)

                }

                if (map.has("errorcode")) {

                    println("Data11--->onNext--->" + map.get("errorcode"))
                }

                if (map.has("data")) {

                    println("Data11--->onNext--" + "data---" + map.get("data").toString())

                    listener.onRequestSuccesful()

                }

                if (map.has("status")) {

                    println("Data11--->onNext--->" + map.get("status"))

                }


            }

            override fun onError(e: Throwable?) {
                listener.onRequestError(context11!!.getString(R.string.serverError))
            }

        }
    }

    interface ConfirmPassRequestListener {

        fun onRequestSuccesful()

        fun onRequestError(error: String)

        fun onError(error: String)


    }

}