package com.denobili.app.teacherAddClasses

import android.content.Context
import com.denobili.app.helper.RxFunctions
import com.denobili.app.helper_utils.DisplayItem
import com.denobili.app.helper_utils.Interactor
import com.crashlytics.android.Crashlytics
import com.vicpin.krealmextensions.delete
import com.vicpin.krealmextensions.query
import com.vicpin.krealmextensions.queryAsObservable
import com.vicpin.krealmextensions.saveAll
import io.realm.RealmQuery
import rx.Observable
import rx.Observer
import java.util.*

class ClassListInteractor
internal constructor(context: Context, private val presenter: ClassListPresenter)
    : Interactor(context) {

    private fun getObservable(): Observable<ClassListApiResponse> =
            apiInterface.getMyClassess(accesstoken, teacher_id, account_id).compose(RxFunctions.applySchedulers())

    private val Observer: Observer<ClassListApiResponse>
        get() = object : Observer<ClassListApiResponse> {
            override fun onCompleted() {
            }

            override fun onError(e: Throwable?) {
                presenter.serverError()
                e?.printStackTrace()
            }

            override fun onNext(t: ClassListApiResponse?) {
                if (t?.status == "Success") {
                    savetoDisk(t.data!!)
                } else presenter.setError(t!!.error_message)
            }
        }

    fun getData() {

       Crashlytics.setUserIdentifier(userid)
       Crashlytics.setUserName(user_name)
       Crashlytics.setUserEmail(user_email)
       if (user_mobileNo != null)
           Crashlytics.setString("Mobile No:", user_mobileNo)

        val lists = ClassListModel().query { realmQuery: RealmQuery<ClassListModel> ->
            realmQuery.equalTo(ClassListModel.UseridKey, account_id).equalTo("active_flag", 1)
        }
        if (lists.isEmpty()) presenter.refreshdata()
        else emitShowList()
    }

    private fun emitShowList() {
        val observable = ClassListModel().queryAsObservable { realmQuery: RealmQuery<ClassListModel> ->
            realmQuery
                    .equalTo(ClassListModel.UseridKey, account_id).equalTo("active_flag", 1)
        }
        observable.compose(RxFunctions.applySchedulers()).subscribe(getDiskObserver())
    }


    private fun savetoDisk(lists: ArrayList<ClassListModel>) {
        val result = ArrayList<ClassListModel>()
        ClassListModel().delete { realmQuery: RealmQuery<ClassListModel> -> realmQuery.equalTo(ClassListModel.UseridKey, account_id) }
        lists.forEachIndexed { index, MyClassModel ->
            var model = MyClassModel
            model.account_id = account_id.toString()
            result.add(index, model)
        }
        result.saveAll()
        emitShowList()
    }

    private fun getDiskObserver(): Observer<List<ClassListModel>> {
        return object : Observer<List<ClassListModel>> {
            override fun onCompleted() {
            }

            override fun onError(e: Throwable?) {
                presenter.serverError()
                e?.printStackTrace()
            }

            override fun onNext(lists: List<ClassListModel>) {
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
