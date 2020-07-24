package com.denobili.app.studentInboxPage

import android.content.Context
import com.denobili.app.helper.RxFunctions
import com.denobili.app.helper_utils.DisplayItem
import com.denobili.app.helper_utils.Interactor
import com.denobili.app.studentallEventsPage.SortbyDateStudent
import com.denobili.app.studentallEventsPage.StudentEventModel
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


    fun getData(page: Int) {
        val list = getDataFromDisk()
        if (list.isNotEmpty())
            getAsDisplayItem(list, 1,1)
        else
            refreshdata(page)
    }

    private fun getAsDisplayItem(list: List<StudentEventModel>, totalPages: Int, totalElements: Int) {
        Collections.sort(list, SortbyDateStudent())
        val result = ArrayList<DisplayItem>(list.size)
        result.addAll(list)
        presenter.setData(result,totalPages,totalElements)
    }

    private fun getDataFromDisk(): List<StudentEventModel> =
            StudentEventModel().query { realmQuery -> realmQuery.equalTo("student_id", student_id).equalTo("event_type", type!!.toInt()) }

    private fun getObservable(page: Int): Observable<StudentInboxApiResponse> =
            apiInterface.getInbox(accesstoken, type!!.toInt(), student_id.toInt(), getCurrentDate(), page,"createDate,desc").compose(RxFunctions.applySchedulers())

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
                    saveToDisk(t.data.content)
                    getAsDisplayItem(getDataFromDisk(),t.data.totalPages,t.data.totalElements)
                } else presenter.setError(t!!.error_status!!)
            }
        }

    fun refreshdata(page:Int) {
        getObservable(page).subscribe(Observer)
    }


}
