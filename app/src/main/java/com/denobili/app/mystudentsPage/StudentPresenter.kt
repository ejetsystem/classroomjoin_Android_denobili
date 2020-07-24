package com.denobili.app.mystudentsPage
import android.content.Context
import com.denobili.app.helper_utils.DisplayItem
import com.denobili.app.helper_utils.Emit
import com.denobili.app.helper_utils.Event
import com.denobili.app.helper_utils.Presenter



class StudentPresenter(context: Context): Presenter() {

   private val interactor: StudentInteractor

   init {
       interactor= StudentInteractor(context,this)
   }

   fun setData(displayItem: ArrayList<DisplayItem>) {
       if(displayItem.isEmpty())
           Emit(StudentEvent(Event.NO_RESULT))
       else Emit(StudentEvent(displayItem))
   }

    fun setError(error:String){
        Emit(StudentEvent(Event.ERROR, error))
    }

    fun serverError(){
        Emit(StudentEvent(Event.SERVER_ERROR))
    }

    fun  getdata(classid:String) {
        interactor.getData(classid)
    }

    fun  deleteitem(studentid: String) {
        interactor.deleteStudent(studentid)
    }

    fun postDeleteditem() {
        Emit(StudentEvent(Event.DELETED_ITEM))
    }

    fun  postDeletionFailure(error_message: String) {
        Emit(StudentEvent(Event.DELETION_FAILURE, error_message))

    }



}