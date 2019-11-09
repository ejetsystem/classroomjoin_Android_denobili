package com.classroomjoin.app.studentDetailPage
import android.content.Context
import com.classroomjoin.app.R
import com.classroomjoin.app.helper_utils.Emit
import com.classroomjoin.app.helper_utils.Event
import com.classroomjoin.app.helper_utils.Presenter


class AddStudentPresenter(private val context: Context): Presenter() {

   private val interactor: AddStudentInteractor

   init {
       interactor= AddStudentInteractor(context,this)
   }

    fun postdata(addStudentModel: AddStudentModel) {
        interactor.postData(addStudentModel)
    }

    fun serverError() {
        Emit(AddStudentEvent(Event.SERVER_ERROR, context.getString(R.string.some_error)))
    }

    fun postsuccess() {
        Emit(AddStudentEvent(Event.POST_SUCCESS))
    }

    fun  setError(error_message: String?) {
        Emit(AddStudentEvent(Event.POST_FAILURE, error_message))
    }

    fun  getdata(studentid: String) {
        interactor.getdata(studentid)
    }

    fun postStudentdetails(student:StudentDetail){
        Emit(AddStudentEvent(Event.RESULT, student))
    }

    fun noresult(error_message: String){
        Emit(AddStudentEvent(Event.NO_RESULT, error_message))
    }


    fun deleteitem(studentid: String){
        interactor.deleteStudent(studentid)
    }


    fun postDeleteditem() {
        Emit(AddStudentEvent(Event.DELETED_ITEM))
    }

    fun  postDeletionFailure(error_message: String) {
        Emit(AddStudentEvent(Event.DELETION_FAILURE, error_message))

    }

    fun isAdmin()=interactor.isAdmin

}