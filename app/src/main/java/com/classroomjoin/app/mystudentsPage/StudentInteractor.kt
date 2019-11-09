package com.classroomjoin.app.mystudentsPage

import android.content.Context
import com.classroomjoin.app.R
import com.classroomjoin.app.helper.RxFunctions
import com.classroomjoin.app.helper_utils.DisplayItem
import com.classroomjoin.app.helper_utils.Interactor
import com.classroomjoin.app.studentDetailPage.DeleteStudentResponse
import rx.Observable
import rx.Observer
import java.util.*


class StudentInteractor
internal constructor(var context: Context, private val presenter: StudentPresenter)
    : Interactor(context) {


    fun getData(classid: String) {
        getObservable(classid).subscribe(Observer)
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
                    val list = ArrayList<DisplayItem>()
                    list.addAll(t.data!!)
                    presenter.setData(list)
                } else presenter.setError(t!!.error_message)

            }
        }

    fun deleteStudent(studentid: String) {
        getDeleteObservable(studentid).subscribe(deleteObserver)
    }

    private fun getDeleteObservable(studentid: String): Observable<DeleteStudentResponse> {
        return apiInterface.deleteStudent(accesstoken, studentid).compose(RxFunctions.applySchedulers())
    }

    private val deleteObserver: Observer<DeleteStudentResponse>
        get() = object : Observer<DeleteStudentResponse> {

            override fun onNext(t: DeleteStudentResponse?) {
                if (t?.status == "Success") {
                    presenter.postDeleteditem()
                } else presenter.postDeletionFailure(context.getString(R.string.serverError))
            }

            override fun onError(e: Throwable?) {
                presenter.postDeletionFailure(context.getString(R.string.serverError))
                e?.printStackTrace()
            }

            override fun onCompleted() {
            }
        }
}
