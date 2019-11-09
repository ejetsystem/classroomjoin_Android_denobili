package com.classroomjoin.app.studentListingPage

import android.content.Context
import com.classroomjoin.app.helper.RxFunctions
import com.classroomjoin.app.helper_utils.DisplayItem
import com.classroomjoin.app.helper_utils.Interactor
import com.classroomjoin.app.mystudentsPage.MyStudentModel
import com.classroomjoin.app.mystudentsPage.StudentApiResponse
import com.vicpin.krealmextensions.delete
import com.vicpin.krealmextensions.query
import com.vicpin.krealmextensions.queryAsObservable
import com.vicpin.krealmextensions.saveAll
import io.realm.RealmQuery
import org.jetbrains.anko.AnkoLogger
import rx.Observable
import rx.Observer


class StudentListingInteractor internal constructor(context: Context, private val presenter: StudentListingPresenter)
    : Interactor(context), AnkoLogger {

    fun getData(classid: String) {
        val lists = MyStudentModel().query { realmQuery: RealmQuery<MyStudentModel> -> realmQuery.equalTo("classid", classid) }
        if (lists.isEmpty()) presenter.refreshdata(classid)
        else emitShowList(classid)
    }


    private fun emitShowList(classid: String) {
        val observable = MyStudentModel().queryAsObservable { realmQuery: RealmQuery<MyStudentModel> -> realmQuery.equalTo("classid", classid) }
        observable.compose(RxFunctions.applySchedulers()).subscribe(getDiskObserver(classid))
    }


    private fun savetoDisk(lists: ArrayList<MyStudentModel>, classid: String) {
        val result = ArrayList<MyStudentModel>()
        MyStudentModel().delete { realmQuery: RealmQuery<MyStudentModel> -> realmQuery.equalTo("classid", classid) }
        lists.forEachIndexed { index, myStudentModel ->
            var model = myStudentModel
            model.classid = classid
            result.add(index, model)
        }
        result.saveAll()
        emitShowList(classid)
    }


    private fun getObserver(classid: String): Observer<StudentApiResponse> {
        return object : Observer<StudentApiResponse> {
            override fun onCompleted() {
            }

            override fun onError(e: Throwable?) {
                presenter.serverError()
                e?.printStackTrace()
            }

            override fun onNext(t: StudentApiResponse?) {
                if (t?.status == "Success") {
                    savetoDisk(t.data!!, classid)
                } else presenter.setError(t!!.error_message)
            }
        }
    }


    private fun getDiskObserver(classid: String): Observer<List<MyStudentModel>> {
        return object : Observer<List<MyStudentModel>> {
            override fun onCompleted() {
            }

            override fun onError(e: Throwable?) {
                presenter.serverError()
                e?.printStackTrace()
            }

            override fun onNext(lists: List<MyStudentModel>) {
                val list = ArrayList<DisplayItem>()
                list.addAll(lists)
                presenter.setData(list)
            }
        }
    }


    fun refresh(classid: String) {
        getObservable(classid).subscribe(getObserver(classid))
    }


    private fun getObservable(classid: String): Observable<StudentApiResponse> {
        return apiInterface.getStudents(accesstoken, account_id, classid).compose(RxFunctions.applySchedulers())
    }

    /*private val Observer:Observer<StudentApiResponse>
        get() = object :Observer<StudentApiResponse>{
            override fun onCompleted() {
            }

            override fun onError(e: Throwable?) {
                presenter.serverError()
                e?.printStackTrace()
            }

            override fun onNext(t: StudentApiResponse?) {
                if(t?.status=="ok"){
                    val list= java.util.ArrayList<DisplayItem>()
                    list.addAll(t.data!!)
                    presenter.setData(list)
                }
                else presenter.setError(t!!.error_message)
            }
        }*/


}
