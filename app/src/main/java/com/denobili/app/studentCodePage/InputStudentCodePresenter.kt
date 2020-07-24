package com.denobili.app.studentCodePage
import android.content.Context
import com.denobili.app.R
import com.denobili.app.helper_utils.Emit
import com.denobili.app.helper_utils.Event
import com.denobili.app.helper_utils.Presenter


class InputStudentCodePresenter(private val context: Context): Presenter() {

   private val interactor: InputStudentViewModel

   init {
       interactor= InputStudentViewModel(context,this)
   }

    fun postdata(addStudentModel: InputCodeModel) {
        interactor.postData(addStudentModel)
    }

    fun serverError() {
        Emit(InputCodeEvent(Event.POST_FAILURE, context.getString(R.string.some_error)))
    }

    fun postsuccess() {
        Emit(InputCodeEvent(Event.POST_SUCCESS))
    }

    fun  setError(error_message: String?) {
        Emit(InputCodeEvent(Event.POST_FAILURE, error_message))
    }



    fun noresult(error_message: String){
        Emit(InputCodeEvent(Event.POST_FAILURE, error_message))
    }




}