package com.classroomjoin.app.studentallEventsPage

import android.content.Context
import com.classroomjoin.app.helper.RxFunctions
import com.classroomjoin.app.helper_utils.DisplayItem
import com.classroomjoin.app.helper_utils.Interactor
import com.vicpin.krealmextensions.delete
import com.vicpin.krealmextensions.query
import com.vicpin.krealmextensions.saveAll
import rx.Observable
import rx.Observer
import java.text.SimpleDateFormat
import java.util.*


class AllEventInteractor
internal constructor(context: Context, private val presenter: All_events_presenter)
    : Interactor(context) {

    fun getData() {
        if (StudentEventModel().query { realmQuery -> realmQuery.equalTo("student_id", student_id).equalTo("type", "0") }.isNotEmpty()) emitList()
        else refreshdata()
    }

    private fun emitList() {
        val list = StudentEventModel().query { realmQuery -> realmQuery.equalTo("student_id", student_id).equalTo("type", "0") }
        Collections.sort(list, SortbyDateStudent())
        val result = ArrayList<DisplayItem>()
        result.addAll(list)
        presenter.setData(result)
    }

    private fun getObservable(): Observable<StudentEventsApiResponse> =
            apiInterface.getEvents(accesstoken, 0, student_id.toInt(), getCurrentDate(), "createDate,desc").compose(RxFunctions.applySchedulers())

    private fun getCurrentDate(): String {
        val c = Calendar.getInstance()
        val df = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        return df.format(c.getTime())
    }

    private fun saveToDisk(list: List<StudentEventModel>) {
        val result = ArrayList<StudentEventModel>()
        StudentEventModel().delete { realmQuery -> realmQuery.equalTo("type", "0").equalTo("student_id", student_id) }
        list.forEachIndexed { index, studentEventModel ->
            studentEventModel.student_id = student_id
            studentEventModel.type = "0"
            result.add(index, studentEventModel)
        }
        result.saveAll()
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
                if (t?.status == "Success") {
                    saveToDisk(t.data)
                    emitList()
                } else presenter.setError(t!!.error_status!!)

            }
        }

    fun refreshdata() {
        getObservable().subscribe(Observer)
    }
}
