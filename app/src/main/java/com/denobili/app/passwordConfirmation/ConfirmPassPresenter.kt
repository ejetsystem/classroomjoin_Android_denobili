package com.denobili.app.passwordConfirmation
import android.content.Context
import com.denobili.app.helper_utils.Emit
import com.denobili.app.helper_utils.Event
import com.denobili.app.helper_utils.Presenter
import org.jetbrains.anko.AnkoLogger


class ConfirmPassPresenter(context: Context): Presenter(),AnkoLogger, ConfirmPassApiManager.ConfirmPassRequestListener {
    var context11:Context?=null


    override fun onRequestSuccesful() {
        Emit(ConfirmPassEvent(Event.POST_SUCCESS))
    }


    override fun onRequestError(error: String) {
        Emit(ConfirmPassEvent(Event.POST_FAILURE, error))
    }

    override fun onError(error: String) {
        Emit(ConfirmPassEvent(Event.ERROR, error))
    }

    private val interactor: ConfirmPassInteractor

   init {
       context11=context
       interactor= ConfirmPassInteractor(context)
   }

    fun postdata(otPverifyModel: ConfirmPassModel){
        interactor.postdata(otPverifyModel,this,context11)
    }

}