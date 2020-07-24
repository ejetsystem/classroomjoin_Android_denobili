package com.denobili.app.mySocialGradesPage
import android.content.Context
import com.denobili.app.helper.NetworkHelper
import com.denobili.app.helper_utils.DisplayItem
import com.denobili.app.helper_utils.Emit
import com.denobili.app.helper_utils.Event
import com.denobili.app.helper_utils.Presenter


class MySocialGradesPresenter(private var context: Context): Presenter() {

   private val interactor: MySocialGradesInteractor

   init {
       interactor= MySocialGradesInteractor(context,this)
   }

   fun setData(displayItem: ArrayList<DisplayItem>) {
       if(displayItem.isEmpty())
           Emit(MySocialGradesEvent(Event.NO_RESULT))
       else Emit(MySocialGradesEvent(displayItem))
   }

    fun setError(error:String){
        Emit(MySocialGradesEvent(Event.ERROR, error))
    }

    fun serverError(){
        Emit(MySocialGradesEvent(Event.SERVER_ERROR))
    }

    fun  getdata() {
        interactor.getData()
    }

    fun refreshdata() {
        if(NetworkHelper.isOnline(context))
            interactor.refresh()
        else Emit(MySocialGradesEvent(Event.NO_INTERNET))
    }

}