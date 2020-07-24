package com.denobili.app.postAttendancePage.checkAttendance

import android.content.Context
import com.denobili.app.helper_utils.Emit
import com.denobili.app.helper_utils.Event
import com.denobili.app.helper_utils.Presenter
import org.jetbrains.anko.AnkoLogger

class AttendanceMicroPresenter(context11: Context) : Presenter(), AnkoLogger, AttendanceMicroApiManager.AttendanceMicroRequestListener {

    private var context: Context? = null


    override fun onRequestSuccesful(error: String) {
        Emit(AttendanceMicroEvent(Event.POST_SUCCESS,error))

    }

    override fun onError(error: String) {
        Emit(AttendanceMicroEvent(Event.ERROR, error))
    }


    override fun onRequestError(error: String) {
        Emit(AttendanceMicroEvent(Event.POST_FAILURE, error))
    }

    private val interactor: AttendanceMicroInteractor

    init {
        context = context11
        interactor = AttendanceMicroInteractor(context11)
    }

    fun postdata(otPverifyModel: AttendanceMicroModel) {
        interactor.postdata(otPverifyModel, this, context)
    }

}