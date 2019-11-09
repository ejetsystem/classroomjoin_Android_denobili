package com.classroomjoin.app.studentEventDetailPage
import android.content.Context
import com.classroomjoin.app.R
import com.classroomjoin.app.helper_utils.Emit
import com.classroomjoin.app.helper_utils.Event
import com.classroomjoin.app.helper_utils.Presenter
import com.classroomjoin.app.studentallEventsPage.StudentEventModel


class EventDetailPresenter(private val context: Context): Presenter() {

   private val interactor: EventDetailInteractor

   init {
       interactor= EventDetailInteractor(context,this)
   }

    fun serverError() {
        Emit(EventDetailFetch(Event.SERVER_ERROR, context.getString(R.string.some_error)))
    }

    fun postsuccess() {
        Emit(EventDetailFetch(Event.POST_SUCCESS))
    }

    fun  setError(error_message: String) {
        Emit(EventDetailFetch(Event.POST_FAILURE, error_message))
    }

    fun  getdata(event_id: Int,eventType_id: String) {
        interactor.getData(event_id,eventType_id)
    }

    fun postEventDetails(event: StudentEventModel){
        Emit(EventDetailFetch(Event.RESULT, event))
    }

    fun noresult(error_message: String){
        Emit(EventDetailFetch(Event.NO_RESULT, error_message))
    }


}