package com.classroomjoin.app.forgotPasswordPage

import android.content.Context
import com.classroomjoin.app.R
import com.classroomjoin.app.helper.RxFunctions
import com.classroomjoin.app.helper_utils.ApiInterface
import com.classroomjoin.app.signUpPage.getOTPsignup.SignupOTPDataReponser
import com.classroomjoin.app.utils.SharedPreferencesData
import com.google.gson.Gson
import com.google.gson.JsonObject
import rx.Observer


class ForgotPasswordApiManager {

    private var context: Context? = null


    fun postdata(forgotPasswordModel: String, apiInterface: ApiInterface, listener: ForgotPasswordRequestListener, context11: Context?) {

        context = context11
        apiInterface.forgotpassword(forgotPasswordModel).compose(RxFunctions.applySchedulers()).subscribe(observer(listener))
    }

    fun observer(listener: ForgotPasswordRequestListener): Observer<JsonObject> =
            object : Observer<JsonObject> {
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

                        val response = Gson().fromJson(map.get("data").toString(), SignupOTPDataReponser::class.java)
                        System.out.println(response.otp)
                        val sharedPreferencesData = SharedPreferencesData(context)
                        sharedPreferencesData.saveForgotOTP(response.otp)
                        println("Data11--->response.otp--->" + response.otp)
                        listener.onRequestSuccesful()

                    }

                    if (map.has("status")) {

                        println("Data11--->onNext--->" + map.get("status"))

                    }


                }

                override fun onError(e: Throwable?) {
                    println("Data22--->onError-->" + "ForgotPasswordReponser")

                    listener.onRequestError(context!!.getString(R.string.serverError))
                }

            }

    interface ForgotPasswordRequestListener {

        fun onRequestSuccesful()

        fun onRequestError(error: String)
        fun onError(error: String)

    }

}