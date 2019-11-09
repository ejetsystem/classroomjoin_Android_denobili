package com.classroomjoin.app.signUpPage
import android.content.Context
import com.classroomjoin.app.R
import com.classroomjoin.app.helper_utils.Emit
import com.classroomjoin.app.helper_utils.Event
import com.classroomjoin.app.helper_utils.Presenter


class SignUpPresenter(private val context: Context): Presenter() {

   private val interactor: SignUpInteractor

   init {
       interactor= SignUpInteractor(context,this)
   }

    fun postdata(model: SignUpModel) {
        interactor.postData(model)
    }

    fun serverError() {
        Emit(SignUpEvent(Event.SERVER_ERROR, context.getString(R.string.some_error)))
    }

    fun postsuccess() {
        Emit(SignUpEvent(Event.POST_SUCCESS))
    }

    fun  setError(error_message: String) {
        Emit(SignUpEvent(Event.POST_FAILURE, error_message))
    }

}