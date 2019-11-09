package com.classroomjoin.app.studentHomePage

import android.content.Context
import com.classroomjoin.app.helper.RxFunctions
import com.classroomjoin.app.helper_utils.DisplayItem
import com.classroomjoin.app.helper_utils.Interactor
import com.classroomjoin.app.studentallEventsPage.StudentEventsApiResponse
import rx.Observable
import rx.Observer
import java.text.SimpleDateFormat
import java.util.*


class StudentEventInteractor
    internal constructor(context: Context, private val presenter: Student_Home_Presenter)
        : Interactor(context) {

    fun getData(studentid:Int,eventid:Int) {
        getObservable(eventid,studentid).subscribe(Observer)
    }

    private fun getObservable(event_type:Int,studentid: Int): Observable<StudentEventsApiResponse> {
        return apiInterface.getEvents(accesstoken,event_type,studentid,getCurrentDate(),"createDate,desc").compose(RxFunctions.applySchedulers())
    }

    private fun getCurrentDate(): String {
        val c = Calendar.getInstance()
        val df = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        return df.format(c.getTime())
    }

    private val Observer: Observer<StudentEventsApiResponse>
             get() = object : Observer<StudentEventsApiResponse> {
                 override fun onCompleted() {
                 }

                 override fun onError(e: Throwable?) {
                     presenter.serverError()
                     e?.printStackTrace()
                 }

                 override fun onNext(t: StudentEventsApiResponse?) {
                     if(t?.status=="Success"){
                         val list= ArrayList<DisplayItem>()
                         list.addAll(t.data)
                         presenter.setData(list)
                     }
                     else presenter.setError(t!!.error_status!!)

                 }
             }
}
