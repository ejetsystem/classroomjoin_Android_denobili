package com.classroomjoin.app.teacherAddClasses
import android.content.Context
import com.classroomjoin.app.helper.NetworkHelper
import com.classroomjoin.app.helper_utils.DisplayItem
import com.classroomjoin.app.helper_utils.Emit
import com.classroomjoin.app.helper_utils.Event
import com.classroomjoin.app.helper_utils.Presenter
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info


class ClassListPresenter(private var context: Context): Presenter(),AnkoLogger {

   private val interactor: ClassListInteractor

   init {
       interactor= ClassListInteractor(context,this)
   }

    fun setData(displayItem: ArrayList<DisplayItem>) {
        info { "called here@@" +displayItem.size+"--->0"+displayItem.isEmpty()}
        if(displayItem.isEmpty())
            Emit(ClassListEvent(Event.NO_RESULT))
        else Emit(ClassListEvent(Event.RESULT,displayItem))
    }

    fun postsuccess(){
        Emit(ClassListEvent(Event.POST_SUCCESS))
    }

    fun postfailure(error: String){
        Emit(ClassListEvent(Event.POST_FAILURE))
    }

    fun setError(error:String){
        Emit(ClassListEvent(Event.ERROR, error))
    }

    fun isAdmin()=interactor.isAdmin

    fun serverError(){
        Emit(ClassListEvent(Event.SERVER_ERROR))
    }

    fun  getdata() {
        interactor.getData()
    }

    fun refreshdata() {
        if(NetworkHelper.isOnline(context))
            interactor.refresh()
        else Emit(ClassListEvent(Event.NO_INTERNET))
    }

}