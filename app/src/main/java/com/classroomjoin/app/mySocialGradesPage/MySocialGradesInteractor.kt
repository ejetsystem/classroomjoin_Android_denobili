package com.classroomjoin.app.mySocialGradesPage

import android.content.Context
import com.classroomjoin.app.helper.RxFunctions
import com.classroomjoin.app.helper_utils.DisplayItem
import com.classroomjoin.app.helper_utils.Interactor
import com.vicpin.krealmextensions.delete
import com.vicpin.krealmextensions.query
import com.vicpin.krealmextensions.queryAsObservable
import com.vicpin.krealmextensions.saveAll
import io.realm.RealmQuery
import rx.Observable
import rx.Observer
import java.util.*


class MySocialGradesInteractor
internal constructor(context: Context, private val presenter: MySocialGradesPresenter)
    : Interactor(context) {


    private fun getObservable(): Observable<MySocialGradesResponse> {
        return apiInterface.getSocialgrades(accesstoken, org_id).compose(RxFunctions.applySchedulers())
    }

    private val Observer: Observer<MySocialGradesResponse>
        get() = object : Observer<MySocialGradesResponse> {
            override fun onCompleted() {
            }

            override fun onError(e: Throwable?) {
                presenter.serverError()
                e?.printStackTrace()
            }

            override fun onNext(t: MySocialGradesResponse?) {
                if (t?.status == "Success") {
                    savetoDisk(t.data)
                } else presenter.setError(t!!.error_response)

            }
        }

    fun getData() {
        val lists = MySocialGradesModel().query { realmQuery: RealmQuery<MySocialGradesModel> -> realmQuery.equalTo("org_id", org_id.toInt()).equalTo("activeFlag", 1) }
        if (lists.isEmpty()) presenter.refreshdata()
        else emitShowList()
    }


    private fun emitShowList() {
        val observable = MySocialGradesModel().queryAsObservable { realmQuery: RealmQuery<MySocialGradesModel> -> realmQuery.equalTo("org_id", org_id.toInt()).equalTo("activeFlag", 1)  }
        observable.compose(RxFunctions.applySchedulers()).subscribe(getDiskObserver())
    }


    private fun savetoDisk(lists: ArrayList<MySocialGradesModel>) {
        val result = ArrayList<MySocialGradesModel>()
        MySocialGradesModel().delete { realmQuery: RealmQuery<MySocialGradesModel> -> realmQuery.equalTo("org_id", org_id.toInt()).equalTo("activeFlag", 1)  }
        lists.forEachIndexed { index, eventType ->
            var model = eventType
            model.org_id = org_id.toInt()
            result.add(index, model)
        }
        result.saveAll()
        emitShowList()
    }


    private fun getDiskObserver(): Observer<List<MySocialGradesModel>> {
        return object : Observer<List<MySocialGradesModel>> {
            override fun onCompleted() {
            }

            override fun onError(e: Throwable?) {
                presenter.serverError()
                e?.printStackTrace()
            }

            override fun onNext(lists: List<MySocialGradesModel>) {
                val list = ArrayList<DisplayItem>()
                list.addAll(lists)
                presenter.setData(list)
            }

        }
    }


    fun refresh() {
        getObservable().subscribe(Observer)
    }


}
