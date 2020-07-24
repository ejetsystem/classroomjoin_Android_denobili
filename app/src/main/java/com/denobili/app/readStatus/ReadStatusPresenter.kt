package com.denobili.app.readStatus

import android.content.Context
import com.denobili.app.helper_utils.Emit
import com.denobili.app.helper_utils.Event
import com.denobili.app.helper_utils.Presenter
import org.jetbrains.anko.AnkoLogger

class ReadStatusPresenter(context11: Context) : Presenter(), AnkoLogger, ReadStatusApiManager.ReadStatusRequestListener {

    private var context: Context? = null


    override fun onRequestSuccesful() {
        Emit(ReadStatusOTPEvent(Event.POST_SUCCESS))

    }

    override fun onError(error: String) {
        Emit(ReadStatusOTPEvent(Event.ERROR, error))
    }


    override fun onRequestError(error: String) {
        Emit(ReadStatusOTPEvent(Event.POST_FAILURE, error))
    }

    private val interactor: ReadStatusInteractor

    init {
        context = context11
        interactor = ReadStatusInteractor(context11)
    }

    fun postdata(otPverifyModel: ReadStatusModel) {
        interactor.postdata(otPverifyModel, this, context)
    }

}