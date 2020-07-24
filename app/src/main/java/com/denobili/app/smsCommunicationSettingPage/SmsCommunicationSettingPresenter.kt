package com.denobili.app.smsCommunicationSettingPage

import android.content.Context
import com.denobili.app.R
import com.denobili.app.emailSettingPage.*
import com.denobili.app.helper_utils.Emit
import com.denobili.app.helper_utils.Event
import com.denobili.app.helper_utils.Presenter
import org.jetbrains.anko.AnkoLogger
import rx.Observer


class SmsCommunicationSettingPresenter(private val context: Context): Presenter(), CommunicationSettingListener,AnkoLogger {
    override fun onCommunicationSettingSuccess() {
        postsuccess()
    }

    override fun onCommunicationSettingFailed(error: String) {
        setError(error)
    }

    override fun onCommunicationSettingFetched(model: List<CommunicationModel>) {
        postCommunicationDetails(model)
    }

    override fun onCommunicationSettingFetchingError(error: String) {
        noresult(error)
    }

    private val interactor: CommunicationSettingInteractor

   init {
       interactor= CommunicationSettingInteractor(context)
   }

    fun postdata(model: CommunicationSettingModel) {
        interactor.postData(model,this)
    }

    fun serverError() {
        Emit(SmsCommunicationSettingEvent(Event.SERVER_ERROR, context.getString(R.string.some_error)))
    }

    fun postsuccess() {
        Emit(SmsCommunicationSettingEvent(Event.POST_SUCCESS))
    }

    fun  setError(error_message: String) {
        Emit(SmsCommunicationSettingEvent(Event.POST_FAILURE, error_message))
    }

    fun  getdata(typeid: String) {
        interactor.getdata(typeid,this)
    }

    fun postCommunicationDetails(model: List<CommunicationModel>){
        Emit(SmsCommunicationSettingEvent(Event.RESULT, model))
    }

    fun noresult(error_message: String){
        Emit(SmsCommunicationSettingEvent(Event.NO_RESULT, error_message))
    }

    fun deactivate(id:Int){
        interactor.deactivate(id).subscribe(setObserver())


    }

    private fun setObserver(): Observer<CommunicationSettingDeactivate> {
        return object : Observer<CommunicationSettingDeactivate> {
            override fun onCompleted() {
            }

            override fun onError(e: Throwable?) {
                Emit(SmsCommunicationSettingEvent(Event.COM_DE_ACTIVATE_FAILED_, context.getString(R.string.some_error)))
                e?.printStackTrace()
            }

            override fun onNext(t: CommunicationSettingDeactivate?) {
                    if(t!!.status=="Success"){
                        Emit(SmsCommunicationSettingEvent(Event.COM_DE_ACTIVATE_SUCCESS))
                    }else
                        Emit(SmsCommunicationSettingEvent(Event.COM_DE_ACTIVATE_FAILED_, context.getString(R.string.some_error)))
            }
        }
    }




}