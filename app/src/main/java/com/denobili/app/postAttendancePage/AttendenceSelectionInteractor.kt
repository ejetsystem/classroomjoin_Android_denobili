package com.denobili.app.postAttendancePage

import android.content.Context
import com.denobili.app.R
import com.denobili.app.helper.RxFunctions
import com.denobili.app.helper_utils.DisplayItem
import com.denobili.app.helper_utils.Interactor
import com.denobili.app.mystudentsPage.MyStudentModel
import com.denobili.app.mystudentsPage.StudentApiResponse
import com.denobili.app.teacherMessageSendPage.SendModelResponse
import com.vicpin.krealmextensions.delete
import com.vicpin.krealmextensions.query
import com.vicpin.krealmextensions.queryAsObservable
import com.vicpin.krealmextensions.saveAll
import io.realm.RealmQuery
import rx.Observable
import rx.Observer


class AttendenceSelectionInteractor
internal constructor(private val context: Context, private val presenter: AttendenceSelectionPresenter)
    : Interactor(context) {

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

    private val Observer: Observer<StudentApiResponse>
        get() = object : Observer<StudentApiResponse> {
            override fun onCompleted() {
            }

            override fun onError(e: Throwable?) {
                presenter.serverError()
                e?.printStackTrace()
            }

            override fun onNext(t: StudentApiResponse?) {
                if (t?.status == "Success") {
                    val list = java.util.ArrayList<DisplayItem>()
                    list.addAll(t.data!!)
                    presenter.setData(list)
                } else presenter.setError(t!!.error_message)
            }
        }

    fun postdata(postAttendence: PostAttendence) {
        postAttendence.accountmanagementid = account_id
        postAttendence.teacherId = teacher_id

        getPostObservable(postAttendence).subscribe(postObserver)
    }

    private fun getPostObservable(postAttendence: PostAttendence): Observable<SendModelResponse> {
        return apiInterface.postattendence(accesstoken, postAttendence).compose(RxFunctions.applySchedulers())
    }

    private val postObserver: Observer<SendModelResponse>
        get() = object : Observer<SendModelResponse> {
            override fun onCompleted() {
            }

            override fun onError(e: Throwable?) {
                presenter.postfailure(context.getString(R.string.serverError))
                e?.printStackTrace()
            }

            override fun onNext(t: SendModelResponse?) {
                if (t!!.status == "Success") {
                    presenter.postSuccess()
                } else {
                    presenter.postfailure(t.error_message)
                }
            }

        }


}
