package com.classroomjoin.app.studentInboxPage

import android.content.Context
import com.classroomjoin.app.helper.RxFunctions
import com.classroomjoin.app.helper_utils.DisplayItem
import com.classroomjoin.app.helper_utils.Interactor
import com.classroomjoin.app.studentallEventsPage.SortbyDateStudent
import com.classroomjoin.app.studentallEventsPage.StudentEventModel
import com.vicpin.krealmextensions.delete
import com.vicpin.krealmextensions.query
import com.vicpin.krealmextensions.saveAll
import rx.Observable
import rx.Observer
import java.text.SimpleDateFormat
import java.util.*


class StudentInboxEventInteractor

internal constructor(context: Context, private val presenter: StudentInboxEventPresenter,type: String)
    : Interactor(context) {
    val type:String?=type


    fun getData() {
        val list = getDataFromDisk()
        if (list.isNotEmpty())
            getAsDisplayItem(list)
        else
            refreshdata()
    }

    private fun getAsDisplayItem(list: List<StudentEventModel>) {
        Collections.sort(list, SortbyDateStudent())
        val result = ArrayList<DisplayItem>(list.size)
        result.addAll(list)
        presenter.setData(result)
    }

    private fun getDataFromDisk(): List<StudentEventModel> =
            StudentEventModel().query { realmQuery -> realmQuery.equalTo("student_id", student_id).equalTo("event_type", type!!.toInt()) }

    private fun getObservable(): Observable<StudentInboxApiResponse> =
            apiInterface.getInbox(accesstoken, type!!.toInt(), student_id.toInt(), getCurrentDate(), "createDate,desc").compose(RxFunctions.applySchedulers())

    private fun getCurrentDate(): String {
        val c = Calendar.getInstance()
        val df = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        return df.format(c.getTime())
    }

    private fun deleteFromDisk() {
        StudentEventModel().delete { realmQuery -> realmQuery.equalTo("event_type", 3).equalTo("student_id", student_id) }
    }

    private fun saveToDisk(list: List<StudentEventModel>) {
        val result = ArrayList<StudentEventModel>()
        list.forEachIndexed { index, studentEventModel ->
            studentEventModel.student_id = student_id
            studentEventModel.event_type = type!!.toInt()
            result.add(index, studentEventModel)
        }
        result.saveAll()
    }

    private val Observer: Observer<StudentInboxApiResponse>
        get() = object : Observer<StudentInboxApiResponse> {
            override fun onCompleted() {
            }

            override fun onError(e: Throwable?) {
                presenter.serverError()
                e?.printStackTrace()
            }

            override fun onNext(t: StudentInboxApiResponse?) {
                if (t?.status == "Success") {
                    deleteFromDisk()
                    saveToDisk(t.data)
                    getAsDisplayItem(getDataFromDisk())
                } else presenter.setError(t!!.error_status!!)
            }
        }

    fun refreshdata( ) {
        getObservable().subscribe(Observer)
    }


}
