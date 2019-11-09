package com.classroomjoin.app.studentSocialGradeReportPage
import android.content.Context
import com.classroomjoin.app.helper_utils.DisplayItem
import com.classroomjoin.app.helper_utils.Emit
import com.classroomjoin.app.helper_utils.Event
import com.classroomjoin.app.helper_utils.Presenter


class StudentSocialGradePresenter(context: Context): Presenter() {

   private val interactor: StudentSocialGradeInteractor

   init {
       interactor= StudentSocialGradeInteractor(context,this)
   }

   fun setData(displayItem: ArrayList<DisplayItem>) {
       if(displayItem.isEmpty())
           Emit(SocialGradeListEvent(Event.NO_RESULT))
       else Emit(SocialGradeListEvent(displayItem))
   }

    fun setError(error:String){
        Emit(SocialGradeListEvent(Event.ERROR, error))
    }

    fun serverError(){
        Emit(SocialGradeListEvent(Event.SERVER_ERROR))
    }

    fun  getdata(month:String,year:String) {
        interactor.getData(month,year)
    }

}