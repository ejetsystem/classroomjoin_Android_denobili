package com.classroomjoin.app.myConnectedStudents

import android.content.Context
import com.classroomjoin.app.R
import com.classroomjoin.app.helper.RxFunctions
import com.classroomjoin.app.helper_utils.DisplayItem
import com.classroomjoin.app.helper_utils.Interactor
import com.classroomjoin.app.helper_utils.RegisterFirebaseResponse
import com.classroomjoin.app.studentCodePage.RegisterFirebaseRequest
import com.classroomjoin.app.studentDetailPage.DeleteStudentResponse
import com.crashlytics.android.Crashlytics
import com.google.firebase.iid.FirebaseInstanceId
import rx.Observable
import rx.Observer
import java.text.SimpleDateFormat
import java.util.*


class ConnectedStudentInteractor
internal constructor(private val context: Context, private val presenter: ConnectedStudentPresenter)
    : Interactor(context) {


    /*fun getData() {
        getObservable().subscribe(Observer)
    }*/

    fun getData1() {

        Crashlytics.setUserIdentifier(userid)
        Crashlytics.setUserName(user_name)
        Crashlytics.setUserEmail(user_email)
        if (user_mobileNo != null)
            Crashlytics.setString("Mobile No:", user_mobileNo)

        getObservable1().subscribe(Observer1)
    }

  /*  private fun getObservable(): Observable<ConnectedStudentApiResponse> =
            apiInterface.getConnectedStudents(accesstoken, connect_students(), teacher_id).compose(RxFunctions.applySchedulers())
*/
    private val Observer: Observer<ConnectedStudentApiResponse>
        get() = object : Observer<ConnectedStudentApiResponse> {
            override fun onCompleted() {
            }

            override fun onError(e: Throwable?) {
                presenter.serverError()
                e?.printStackTrace()
            }

            override fun onNext(t: ConnectedStudentApiResponse?) {
                if (t?.status == "Success") {
                    val list = ArrayList<DisplayItem>()
                    list.addAll(t.data!!)
                    var st_codes: ArrayList<Int>? = ArrayList<Int>()
                    t.data?.forEachIndexed { index, myConnectedStudentModel ->
                        st_codes?.add(myConnectedStudentModel.id!!)
                    }

                    presenter.setData(list)
                } else presenter.setError(t!!.error_message)

            }
        }


    private fun getObservable1(): Observable<ConnectedStudentApiResponse> =
            apiInterface.getParentStudentList(accesstoken, teacher_id).compose(RxFunctions.applySchedulers())

    private val Observer1: Observer<ConnectedStudentApiResponse>
        get() = object : Observer<ConnectedStudentApiResponse> {
            override fun onCompleted() {
            }

            override fun onError(e: Throwable?) {
                presenter.serverError()
                e?.printStackTrace()
            }

            override fun onNext(t: ConnectedStudentApiResponse?) {
                if (t?.status == "Success") {
                    val list = ArrayList<DisplayItem>()
                   // if(t.data!!.)
                    //list.addAll(t.data!!)
                    var st_codes: ArrayList<Int>? = ArrayList<Int>()
                    t.data?.forEachIndexed { index, myConnectedStudentModel ->
                        st_codes?.add(myConnectedStudentModel.id!!)
                        if(myConnectedStudentModel.active_flag==1)
                            list.add(myConnectedStudentModel)
                    }
                    presenter.setData(list)
                } else presenter.setError(t!!.error_message)

            }
        }

    fun deleteStudent(studentid: String) {
        getDeleteObservable(studentid).subscribe(deleteObserver)
    }

    private fun getDeleteObservable(studentid: String): Observable<DeleteStudentResponse> =
            apiInterface.deleteStudent(accesstoken, studentid).compose(RxFunctions.applySchedulers())

    private val deleteObserver: Observer<DeleteStudentResponse>
        get() = object : Observer<DeleteStudentResponse> {

            override fun onNext(t: DeleteStudentResponse?) {
                if (t?.status == "Success") {
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

    fun registerFirebase(request: RegisterFirebaseRequest) {
        getFirebaseObservable(request).subscribe(firebaseObserver)
    }

    fun getFirebaseObservable(request: RegisterFirebaseRequest): Observable<RegisterFirebaseResponse> =
            apiInterface.registerFirebase(accesstoken, request).compose(RxFunctions.applySchedulers())

    private val firebaseObserver: Observer<RegisterFirebaseResponse>
        get() = object : Observer<RegisterFirebaseResponse> {
            override fun onCompleted() {
            }

            override fun onError(e: Throwable?) {
                e?.printStackTrace()
            }

            override fun onNext(t: RegisterFirebaseResponse?) {
                if (t?.status == "Success") {
                } else {
                }

            }
        }

    fun register(model: MyConnectedStudentModel) {
        student_id = model.id.toString()

        if (!model.firstName.isNullOrBlank())
            student_name = model.firstName

        if (!model.middleName.isNullOrBlank())
            student_name = student_name + " " + model.middleName

        if (!model.lastName.isNullOrBlank())
            student_name = student_name + " " + model.lastName
       // student_name = model.firstName + " " + model.middleName + " " + model.lastName
        classname = model.classname
        registerFirebase(RegisterFirebaseRequest(FirebaseInstanceId.getInstance().token!!,
                userid,
                arrayListOf(model.id!!), getCurrentDate()))

    }

    fun connect_students(): String {
        if (user_type == 4) return "2" else return "1"
    }

    private fun getCurrentDate(): String {
        val c = Calendar.getInstance()
        val df = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        return df.format(c.getTime())
    }

}
