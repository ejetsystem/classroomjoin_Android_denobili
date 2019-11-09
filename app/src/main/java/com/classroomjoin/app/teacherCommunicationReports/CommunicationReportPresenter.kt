package com.classroomjoin.app.teacherCommunicationReports
import android.content.Context
import com.classroomjoin.app.helper_utils.DisplayItem
import com.classroomjoin.app.helper_utils.Emit
import com.classroomjoin.app.helper_utils.Event
import com.classroomjoin.app.helper_utils.Presenter
import org.jetbrains.anko.AnkoLogger


class CommunicationReportPresenter(context: Context): Presenter(),AnkoLogger {

   private val interactor: CommunicationReportInteractor

   init {
       interactor= CommunicationReportInteractor(context,this)
   }

    fun setData(displayItem: ArrayList<DisplayItem>) {
        if(displayItem.isEmpty())
            Emit(CommunicationReportEvent(Event.NO_RESULT))
        else {
            //displayItem.add(0,ReportModel("Studentname","Date  & Time","Message"))
            Emit(CommunicationReportEvent(displayItem))
        }
    }

    fun postsuccess(){
        Emit(CommunicationReportEvent(Event.POST_SUCCESS))
    }

    fun postfailure(error: String){
        Emit(CommunicationReportEvent(Event.POST_FAILURE))
    }

    fun setError(error:String){
        Emit(CommunicationReportEvent(Event.ERROR, error))
    }

    fun serverError(){
        Emit(CommunicationReportEvent(Event.SERVER_ERROR))
    }

    fun  getdata(type:String,classid:String) {
        interactor.getData(type,classid)
    }

    fun getdata(type: String,classid: String,query:String){
        interactor.getData(type,classid,query)
    }

    fun  refreshData(type: String,classid: String){
        interactor.refreshData(type,classid)
    }



}