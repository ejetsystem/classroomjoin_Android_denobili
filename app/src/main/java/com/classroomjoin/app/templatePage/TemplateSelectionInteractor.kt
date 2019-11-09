package com.classroomjoin.app.templatePage

import android.content.Context
import com.classroomjoin.app.helper.RxFunctions
import com.classroomjoin.app.helper_utils.DisplayItem
import com.classroomjoin.app.helper_utils.Interactor
import com.vicpin.krealmextensions.delete
import com.vicpin.krealmextensions.query
import com.vicpin.krealmextensions.saveAll
import io.realm.RealmQuery
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.error
import rx.Observable
import rx.Observer


class TemplateSelectionInteractor
internal constructor(context: Context, private val presenter: TemplateSelectionPresenter)
    : Interactor(context), AnkoLogger {

    fun getData(typeid: Int) {

        //println("Data--->getData-->"+getItems(typeid).isEmpty())

        if (getItems(typeid).isEmpty())
            presenter.refreshdata(typeid)
        else {
            getResults(typeid)
        }
    }

    fun getResults(typeid: Int) {
        //println("Data--->getItems(typeid).size-->"+getItems(typeid).size)

        val result = ArrayList<DisplayItem>(getItems(typeid).size)
        result.addAll(getItems(typeid))
        presenter.setData(result)
    }

    fun getItems(typeid: Int): List<Template> {

        //println("Data--->typeid-->"+typeid+"--org_id--->"+ org_id.toInt())
        if (typeid > 3)
            return Template().query { realmQuery: RealmQuery<Template> ->
            realmQuery.beginGroup().equalTo("account_id", account_id.toInt()).endGroup()
                    .beginGroup()
                    .equalTo("type_id", 4)
                    .or().equalTo("type_id", 5)
                    .endGroup()
        }
        else return Template().query { realmQuery: RealmQuery<Template> ->
            realmQuery.equalTo("type_id", typeid).equalTo("orgid", org_id.toInt())
        }
    }

    fun delete(typeid: Int) {
        if (typeid > 3)
            Template().delete { realmQuery: RealmQuery<Template> ->
            realmQuery.beginGroup().equalTo("account_id", account_id.toInt()).endGroup()
                    .beginGroup()
                    .equalTo("type_id", 4)
                    .or().equalTo("type_id", 5)
                    .endGroup()
        }
        else Template().delete { realmQuery: RealmQuery<Template> ->
            realmQuery.equalTo("type_id", typeid).equalTo("orgid", org_id.toInt())
        }
    }

    private fun getObservable(typeid: Int): Observable<TemplateSelectionApiResponse> =
            apiInterface.getTemplates(accesstoken, org_id, typeid).compose(RxFunctions.applySchedulers())


    private fun savetoDisk(lists: ArrayList<Template>, typeid: Int) {

        //println("Data--->+savetoDisk11--->"+lists.size)

        delete(typeid)
       // println("Data--->+savetoDisk22--->"+lists.size)

        lists.saveAll()
        getResults(typeid)
    }

    private fun getObserver(typeid: Int): Observer<TemplateSelectionApiResponse> {
        return object : Observer<TemplateSelectionApiResponse> {
            override fun onCompleted() {
            }

            override fun onError(e: Throwable?) {
                presenter.serverError()
                e?.printStackTrace()
            }

            override fun onNext(t: TemplateSelectionApiResponse?) {
                if (t?.status == "Success") {

                    //println("Data--->+TemplateSelectionApiResponse--->"+t.data.size)
                    savetoDisk(t.data, typeid)


                } else presenter.setError(t!!.error_response)
            }
        }
    }

    private fun getDiskObserver(typeid: Int): Observer<List<Template>> {
        return object : Observer<List<Template>> {
            override fun onCompleted() {
            }

            override fun onError(e: Throwable?) {
                presenter.serverError()
                e?.printStackTrace()
            }

            override fun onNext(lists: List<Template>) {
                val list = ArrayList<DisplayItem>()
                list.addAll(lists)
                presenter.setData(list)
            }
        }
    }

    fun refresh(typeid: Int) {
        error { "Refreshing data of Template" }
        getObservable(typeid).subscribe(getObserver(typeid))
    }
}
