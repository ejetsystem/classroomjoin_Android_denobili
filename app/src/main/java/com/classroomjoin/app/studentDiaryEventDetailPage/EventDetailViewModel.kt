package com.classroomjoin.app.studentDiaryEventDetailPage

import android.content.Context
import com.classroomjoin.app.R
import com.classroomjoin.app.helper.RxFunctions
import com.classroomjoin.app.helper_utils.Interactor
import com.classroomjoin.app.studentDiaryPage.StudentDiaryEventModel
import com.vicpin.krealmextensions.createOrUpdate
import com.vicpin.krealmextensions.queryAsObservable
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.error
import rx.Observable

class EventDetailViewModel
internal constructor(private val context: Context)
    : Interactor(context), AnkoLogger {

    fun getDataFromRemote(eventid: Int,eventType_id:String): Observable<StudentDiaryEventModel> {
        error { "get data from remote called" }
        return apiInterface.getDiaryEventDetails(accesstoken, eventid,eventType_id)
                .compose(RxFunctions.applySchedulers())
                .filter { t: StudentDiaryEventApiResponse? ->
                    if (t!!.status == "Success") true
                    else throw Throwable(t.error_status)
                }
                .switchMap { t -> Observable.just(t.data) }
                .doOnNext { t: StudentDiaryEventModel? ->
                    if (t != null) saveToDisk(t, eventid)
                    else throw Throwable(context.getString(R.string.noResults))
                }
    }

    fun getDataFromDisk(eventid: Int): Observable<StudentDiaryEventModel>? {
        error { "get data from disk called" }
        try {
            return StudentDiaryEventModel().queryAsObservable { realmQuery -> realmQuery.equalTo("id", 1234) }.first()
                    .filter { t: List<StudentDiaryEventModel>? -> t?.isNotEmpty() }
                    .switchMap { t: List<StudentDiaryEventModel> -> Observable.just(t[0]) }
                    .onErrorReturn {
                        throw Throwable(context.getString(R.string.noResults))
                    }
        } catch (e: Exception) {
            error { "exceptuon thrown at database fetch" }
            throw Throwable(context.getString(R.string.noResults))
        }
    }

    fun saveToDisk(model: StudentDiaryEventModel, eventid: Int) {
        error { "called here to save the data" }
        model.createOrUpdate()
    }


}