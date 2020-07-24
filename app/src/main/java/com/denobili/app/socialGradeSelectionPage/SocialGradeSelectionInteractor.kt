package com.denobili.app.socialGradeSelectionPage

import android.content.Context
import com.denobili.app.R
import com.denobili.app.helper.RxFunctions
import com.denobili.app.helper_utils.DisplayItem
import com.denobili.app.helper_utils.Interactor
import com.denobili.app.mySocialGradesPage.MySocialGradesModel
import com.denobili.app.mySocialGradesPage.MySocialGradesResponse
import com.denobili.app.teacherMessageSendPage.SendModelResponse
import com.vicpin.krealmextensions.query
import com.vicpin.krealmextensions.queryAsObservable
import com.vicpin.krealmextensions.saveAll
import io.realm.RealmQuery
import rx.Observable
import rx.Observer
import java.util.*


class SocialGradeSelectionInteractor
internal constructor(private val context: Context, private val presenter: SocialGradesSelectionPresenter)
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
        val lists = MySocialGradesModel().query { realmQuery: RealmQuery<MySocialGradesModel> -> realmQuery.equalTo("org_id", org_id.toInt()) }
        if (lists.isEmpty()) presenter.refreshdata()
        else emitShowList()
    }


    private fun emitShowList() {
        val observable = MySocialGradesModel().queryAsObservable { realmQuery: RealmQuery<MySocialGradesModel> -> realmQuery.equalTo("org_id", org_id.toInt()) }
        observable.compose(RxFunctions.applySchedulers()).subscribe(getDiskObserver())
    }


    private fun savetoDisk(lists: ArrayList<MySocialGradesModel>) {
        val result = ArrayList<MySocialGradesModel>()
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

    fun postData(model: SocialGradeSendModel) {
        getSendObservable(model).subscribe(SendObserver)
    }

    private fun getSendObservable(model: SocialGradeSendModel): Observable<SendModelResponse> {
        model.acc_id = account_id
        model.orgid = org_id
        return apiInterface.sendSocialGrades(accesstoken, model).compose(RxFunctions.applySchedulers())
    }

    private val SendObserver: Observer<SendModelResponse>
        get() = object : Observer<SendModelResponse> {
            override fun onCompleted() {
            }

            override fun onError(e: Throwable?) {
                presenter.postFailure(context.getString(R.string.some_error))
                e?.printStackTrace()
            }

            override fun onNext(t: SendModelResponse?) {
                if (t?.status == "Success") {
                    presenter.postsuccess()
                } else presenter.postFailure(t!!.error_message)

            }
        }
}
