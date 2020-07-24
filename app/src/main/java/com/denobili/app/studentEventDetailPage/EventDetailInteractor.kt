package com.denobili.app.studentEventDetailPage

import android.content.Context
import com.denobili.app.R
import com.denobili.app.helper.RxFunctions
import com.denobili.app.helper_utils.Interactor
import com.denobili.app.studentallEventsPage.StudentEventModel
import com.vicpin.krealmextensions.createOrUpdate
import com.vicpin.krealmextensions.query
import rx.Observable
import rx.Observer


class EventDetailInteractor
    internal constructor(private val context: Context, private val presenter: EventDetailPresenter)
        : Interactor(context) {

    private fun getObservable(eventid: Int,eventType_id: String):Observable<StudentEventApiResponse> =
            apiInterface.getEventDetails(accesstoken,eventid,eventType_id).compose(RxFunctions.applySchedulers())

    private fun detailsObserver(eventid: Int):Observer<StudentEventApiResponse> {
        return object : Observer<StudentEventApiResponse> {
            override fun onNext(t: StudentEventApiResponse) {
                if (t.status == "Success") {
                    saveToDisk(t.data,eventid)
                    getDataFromDisk(eventid)
                } else presenter.noresult(t.error_status)
            }

            override fun onError(e: Throwable?) {
                presenter.noresult(context.getString(R.string.some_error))
                e?.printStackTrace()
            }

            override fun onCompleted() {
            }
        }
    }


    fun getData(eventid:Int,eventType_id: String) {
        if(StudentEventModel().query { realmQuery -> realmQuery.equalTo("id",eventid)}.isNotEmpty()) getDataFromDisk(eventid)
        else refreshdata(eventid,eventType_id)
    }

    private fun getDataFromDisk(eventid:Int){
        val result= StudentEventModel().query { realmQuery -> realmQuery.equalTo("id",eventid) }.first()
        presenter.postEventDetails(result)
    }

    private fun saveToDisk(model: StudentEventModel,eventid: Int){
        model.createOrUpdate()
    }

    fun refreshdata(eventid: Int,eventType_id: String){
        getObservable(eventid,eventType_id).subscribe(detailsObserver(eventid))
    }
}
