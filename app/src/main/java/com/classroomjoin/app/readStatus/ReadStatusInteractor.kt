package com.classroomjoin.app.readStatus

import android.content.Context
import com.classroomjoin.app.helper_utils.Interactor

class ReadStatusInteractor
internal constructor(context: Context)
    : Interactor(context) {

    fun postdata(otPverifyModel: ReadStatusModel, listener: ReadStatusApiManager.ReadStatusRequestListener, context: Context?) {
        val manager = ReadStatusApiManager()
        manager.postdata(accesstoken, otPverifyModel, apiInterface, listener, context)

    }


}