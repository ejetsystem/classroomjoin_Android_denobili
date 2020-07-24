package com.denobili.app.teacherSocialGradeReport
import android.content.Context
import com.denobili.app.helper_utils.DisplayItem
import com.denobili.app.helper_utils.Emit
import com.denobili.app.helper_utils.Event
import com.denobili.app.helper_utils.Presenter
import org.jetbrains.anko.AnkoLogger


class ClassSocialGradePresenter(context: Context): Presenter(),AnkoLogger {

   private val interactor: ClassSocialGradeInteractor

   init {
       interactor= ClassSocialGradeInteractor(context,this)
   }

   fun setData(displayItem: ArrayList<DisplayItem>) {
       if(displayItem.isEmpty()) Emit(ClassSocialGradeSummaryEvent(Event.NO_RESULT))
       else{
           Emit(ClassSocialGradeSummaryEvent(displayItem))
       }
   }

    fun setError(error:String){
        Emit(ClassSocialGradeSummaryEvent(Event.ERROR, error))
    }

    fun serverError(){
        Emit(ClassSocialGradeSummaryEvent(Event.SERVER_ERROR))
    }

    fun  getdata(classid:String) {
        interactor.getData(classid)
    }

}