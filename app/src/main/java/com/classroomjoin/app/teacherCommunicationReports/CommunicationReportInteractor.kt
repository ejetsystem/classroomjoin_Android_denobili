package com.classroomjoin.app.teacherCommunicationReports

import android.content.Context
import com.classroomjoin.app.helper.RxFunctions
import com.classroomjoin.app.helper_utils.DisplayItem
import com.classroomjoin.app.helper_utils.Interactor
import com.vicpin.krealmextensions.delete
import com.vicpin.krealmextensions.query
import com.vicpin.krealmextensions.queryAsObservable
import com.vicpin.krealmextensions.saveAll
import io.realm.Case
import io.realm.RealmQuery
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.error
import rx.Observable
import rx.Observer
import java.text.SimpleDateFormat
import java.util.*

class CommunicationReportInteractor
internal constructor(context: Context, private val presenter: CommunicationReportPresenter)
    : Interactor(context), AnkoLogger {


    fun getData(type_id: String, classid: String) {
        val lists = ReportModel().query { realmQuery: RealmQuery<ReportModel> -> realmQuery.equalTo("class_id", classid).equalTo("type_id", type_id) }
        if (lists.isEmpty()) presenter.refreshData(type_id, classid)
        else getDataFromDisk(type_id, classid)
    }


    fun getData(type_id: String, classid: String, query: String) {
        error { "query is " + query }
        val observable = ReportModel().queryAsObservable { realmQuery: RealmQuery<ReportModel> ->
            realmQuery.beginGroup() // <--
                    .equalTo("class_id", classid)
                    .endGroup() // <--
                    .beginGroup() // <--
                    .equalTo("type_id", type_id)
                    .endGroup() // <--
                    .beginGroup() //
                    .contains("message", query, Case.INSENSITIVE)
                    .or()
                    .contains("student_name", query, Case.INSENSITIVE)
                    .endGroup()
                    .findAll()
        }
        observable.compose(RxFunctions.applySchedulers()).subscribe(getDiskObserver(classid))
    }


    private fun getDataFromDisk(type_id: String, classid: String) {
        val observable = ReportModel().queryAsObservable { realmQuery: RealmQuery<ReportModel> -> realmQuery.equalTo("class_id", classid).equalTo("type_id", type_id) }
        observable.compose(RxFunctions.applySchedulers()).subscribe(getDiskObserver(classid))
    }


    private fun savetoDisk(lists: List<ReportModel>?, classid: String, type_id: String) {
        val result = ArrayList<ReportModel>()
        ReportModel().delete { realmQuery: RealmQuery<ReportModel> -> realmQuery.equalTo("class_id", classid).equalTo("type_id", type_id) }
        lists!!.forEachIndexed { index, myStudentModel ->
            var model = myStudentModel
            model.class_id = classid
            model.type_id = type_id
            result.add(index, model)
        }
        result.saveAll()
        getDataFromDisk(type_id, classid)
    }

    private fun getDiskObserver(classid: String): Observer<List<ReportModel>> {
        return object : Observer<List<ReportModel>> {
            override fun onCompleted() {
            }

            override fun onError(e: Throwable?) {
                presenter.serverError()
                e?.printStackTrace()
            }

            override fun onNext(lists: List<ReportModel>) {
                error { "size is " + lists.size }
                Collections.sort(lists, SortbyDate())
                val list = ArrayList<DisplayItem>()
                list.addAll(lists)
                presenter.setData(list)
            }
        }
    }


    private fun getEmailObservable(classid: String): Observable<ReportReponse> =
            apiInterface.getCommunicationReportsEmail(accesstoken, account_id, classid, getCurrentDate(), "createDate").compose(RxFunctions.applySchedulers())

    private fun getMessageObservable(classid: String): Observable<ReportReponse> =
            apiInterface.getCommunicationReportsMessage(accesstoken, account_id, classid, getCurrentDate(), "createDate").compose(RxFunctions.applySchedulers())


    private fun getSms(classid: String): Observable<ReportReponse> =
            apiInterface.getCommunicationReportsSMS(accesstoken, account_id, classid, getCurrentDate(), "createDate").compose(RxFunctions.applySchedulers())

    private fun Observer(classid: String, type_id: String) = object : Observer<ReportReponse> {
        override fun onCompleted() {
        }

        override fun onError(e: Throwable?) {
            presenter.serverError()
            e?.printStackTrace()
        }

        override fun onNext(t: ReportReponse?) {
            if (t?.status == "Success") {
                //val list = ArrayList<DisplayItem>()
                //list.addAll(t.data.content)
                savetoDisk(t.data.content, classid, type_id)
            } else presenter.setError(t!!.message)

        }
    }


    fun refreshData(type: String, classid: String) {
        if (type == "1") getEmailObservable(classid).subscribe(Observer(classid, type))
        else if (type == "3") getMessageObservable(classid).subscribe(Observer(classid, type))
        else getSms(classid).subscribe(Observer(classid, type))

    }


    private fun getCurrentDate(): String {
        val c = Calendar.getInstance()
        val df = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        return df.format(c.getTime())
    }

}
