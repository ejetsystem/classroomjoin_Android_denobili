package com.denobili.app.invoice

import android.content.Context
import com.denobili.app.R
import com.denobili.app.helper.NetworkConnectionInterceptor
import com.denobili.app.helper.RxFunctions
import com.denobili.app.helper_utils.ApiInter
import com.denobili.app.helper_utils.DisplayItem
import com.denobili.app.helper_utils.Interactor
import com.denobili.app.helper_utils.RegisterFirebaseResponse
import com.denobili.app.myConnectedStudents.ConnectedStudentApiResponse
import com.denobili.app.studentCodePage.RegisterFirebaseRequest
import com.denobili.app.studentDetailPage.DeleteStudentResponse
import com.crashlytics.android.Crashlytics
import rx.Observable
import rx.Observer
import java.text.SimpleDateFormat
import java.util.*

class InvoiceInteractor internal constructor(private val context: Context, private val presenter: InvoicePresenter)
    : Interactor(context) {
     var apiInter:ApiInter?=null
var orgId:String?=null



    /*fun getData() {
        getObservable().subscribe(Observer)
    }*/

    fun getData1() {
        Crashlytics.setUserIdentifier(userid)
        Crashlytics.setUserName(user_name)
        Crashlytics.setUserEmail(user_email)
        if (user_mobileNo != null)
            Crashlytics.setString("Mobile No:", user_mobileNo)
        if(org_id=="58"){
            orgId="https://ctps.edustacks.org/"
        }
        else if(org_id=="57"){
            orgId="https://gomoh.edustacks.org/"
        }
        else if(org_id=="56"){
            orgId="https://cmri.edustacks.org/"
        }
        else if(org_id=="55"){
            orgId="https://bhuli1.edustacks.org/"
        }
        else if(org_id=="54"){
            orgId="https://koradih.edustacks.org/"
        }
        else if(org_id=="51"){
            orgId="https://maithon.edustacks.org/"
        }
        else if(org_id=="41"){
            orgId="https://mugma.edustacks.org/"
        }
        val networkConnectionInterceptor = NetworkConnectionInterceptor(context)
        apiInter = ApiInter(networkConnectionInterceptor,orgId.toString())
        getObservable1().subscribe(Observer1)

    }

    /* *//*  private fun getObservable(): Observable<ConnectedStudentApiResponse> =
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
                } else
                    presenter.setError(t!!.error_message)

            }
        }


    private fun getObservable1(): Observable<InputInvoiceResponse> =
            apiInter?.getParentStudentList("fees/api", teacher_id,student_id)?.compose(RxFunctions.applySchedulers())!!

    private val Observer1: Observer<InputInvoiceResponse>
        get() = object : Observer<InputInvoiceResponse> {
            override fun onCompleted() {
            }

            override fun onError(e: Throwable?) {
                presenter.serverError()
                e?.printStackTrace()
            }

            override fun onNext(t: InputInvoiceResponse?) {
                if (t?.status == "Success") {
                    val list = ArrayList<DisplayItem>()
                    // if(t.data!!.)
                    //list.addAll(t.data!!)
                    var st_codes: ArrayList<Int>? = ArrayList<Int>()
                    t.data?.forEachIndexed { index, myConnectedStudentModel ->
                        st_codes?.add(myConnectedStudentModel.id!!)

                            list.add(myConnectedStudentModel)
                    }
                    presenter.setData(list)
                }// else{}

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

    fun register(model: ConnectedListModel) {
        student_id = model.id.toString()

        if (!model.studentFullName.isNullOrBlank())
            student_name = model.studentFullName
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
