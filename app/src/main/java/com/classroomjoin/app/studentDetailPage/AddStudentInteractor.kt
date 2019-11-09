package com.classroomjoin.app.studentDetailPage

import android.content.Context
import com.classroomjoin.app.R
import com.classroomjoin.app.helper.RxFunctions
import com.classroomjoin.app.helper_utils.Interactor
import com.classroomjoin.app.mystudentsPage.MyStudentModel
import com.classroomjoin.app.teacherAddClasses.ClassListModel
import com.classroomjoin.app.teacherOutboxPage.OutboxModel
import com.vicpin.krealmextensions.createOrUpdate
import com.vicpin.krealmextensions.delete
import com.vicpin.krealmextensions.query
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.error
import rx.Observable
import rx.Observer
import java.text.SimpleDateFormat
import java.util.*


class AddStudentInteractor
internal constructor(private val context: Context, private val presenter: AddStudentPresenter)
    : Interactor(context), AnkoLogger {

    fun postData(addStudentModel: AddStudentModel) {
        if (addStudentModel.student_id == null)
            getObservable(addStudentModel).subscribe(addObserver(addStudentModel.class_id))
        else
            geteditObservable(addStudentModel).subscribe(Observer)
    }

    private fun getObservable(addStudentModel: AddStudentModel): Observable<AddStudentResponse> {
        addStudentModel.teacher_id = teacher_id
        addStudentModel.org_id = org_id
        addStudentModel.accountManagementId = account_id

        return apiInterface.addStudent(accesstoken, addStudentModel).compose(RxFunctions.applySchedulers())
    }

    private fun geteditObservable(addStudentModel: AddStudentModel): Observable<AddStudentResponse> {
        addStudentModel.teacher_id = teacher_id
        addStudentModel.org_id = org_id
        return apiInterface.editStudent(accesstoken, addStudentModel.student_id!!, addStudentModel.name, "", "",addStudentModel.parentMobile,
                addStudentModel.admission_id!!.toInt(), addStudentModel.roll_no!!.toInt(), getCurrentDate()
        ).compose(RxFunctions.applySchedulers())
    }

    private fun getCurrentDate(): String {
        val c = Calendar.getInstance()
        val df = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        return df.format(c.getTime())
    }


    private fun addObserver(classid: String?): Observer<AddStudentResponse> {
        return object : Observer<AddStudentResponse> {
            override fun onCompleted() {
            }

            override fun onError(e: Throwable?) {
                presenter.serverError()
                e?.printStackTrace()
            }

            override fun onNext(t: AddStudentResponse?) {
                if (t?.status == "Success") {
                    presenter.postsuccess()
                    var model = ClassListModel().query { realmQuery -> realmQuery.equalTo("id", classid!!.toInt()) }.first()
                    model.no_of_students += 1
                    model.createOrUpdate()
                } else presenter.setError(t!!.error_message)

            }
        }
    }

    private val Observer: Observer<AddStudentResponse>
        get() = object : Observer<AddStudentResponse> {
            override fun onCompleted() {
            }

            override fun onError(e: Throwable?) {
                presenter.serverError()
                e?.printStackTrace()
            }

            override fun onNext(t: AddStudentResponse?) {
                if (t?.status == "Success") {
                    presenter.postsuccess()
                } else presenter.setError(t!!.error_message)

            }
        }

    fun getdata(studentid: String) {
        error { "student id " + studentid }
        getObservable(studentid).subscribe(detailsObserver)
    }

    private fun getObservable(studentid: String): Observable<StudentDetailsApiResponse> {
        return apiInterface.getStudentDetails(accesstoken, studentid).compose(RxFunctions.applySchedulers())
    }

    private val detailsObserver: Observer<StudentDetailsApiResponse>
        get() = object : Observer<StudentDetailsApiResponse> {

            override fun onNext(t: StudentDetailsApiResponse?) {
                if (t?.status == "Success") {
                    presenter.postStudentdetails(t.student)
                } else presenter.noresult(t!!.error_message)
            }

            override fun onError(e: Throwable?) {
                presenter.setError(context.getString(R.string.some_error))
                e?.printStackTrace()
            }

            override fun onCompleted() {
            }
        }


    fun deleteStudent(studentid: String) {
        error { "student id " + studentid }
        val list = OutboxModel().query { query -> query.equalTo("account_id", account_id) }
        if (list.isEmpty()) getDeleteObservable(studentid).subscribe(deleteObserver(studentid))
        else presenter.postDeletionFailure(context.getString(R.string.unsend_outbox_items))

    }

    private fun getDeleteObservable(studentid: String): Observable<DeleteStudentResponse> {
        return apiInterface.deleteStudent(accesstoken, studentid).compose(RxFunctions.applySchedulers())
    }

    private fun deleteObserver(id: String) = object : Observer<DeleteStudentResponse> {
        override fun onNext(t: DeleteStudentResponse?) {
            if (t?.status == "Success") {
                try {
                    MyStudentModel().delete { realmQuery -> realmQuery.equalTo("id", id.toInt()) }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                presenter.postDeleteditem()

            } else presenter.postDeletionFailure(context.getString(R.string.some_error))
        }

        override fun onError(e: Throwable?) {
            presenter.postDeletionFailure(context.getString(R.string.some_error))
            e?.printStackTrace()
        }

        override fun onCompleted() {
        }
    }

    private fun deleteItemDatabase(studentid: String) {
        error { "student id " + studentid }
        /*var model=ClassListModel().query { realmQuery ->realmQuery.equalTo("id",student.classid)}.first()
        model.no_of_students=model.no_of_students-1
        model.createOrUpdate()*/


    }


}
