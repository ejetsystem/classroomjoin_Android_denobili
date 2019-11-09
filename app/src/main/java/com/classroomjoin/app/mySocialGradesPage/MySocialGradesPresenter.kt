package com.classroomjoin.app.mySocialGradesPage
import android.content.Context
import com.classroomjoin.app.helper.NetworkHelper
import com.classroomjoin.app.helper_utils.DisplayItem
import com.classroomjoin.app.helper_utils.Emit
import com.classroomjoin.app.helper_utils.Event
import com.classroomjoin.app.helper_utils.Presenter


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