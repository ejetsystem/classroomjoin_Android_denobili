package com.classroomjoin.app.studentCodePage

import android.content.Context
import com.classroomjoin.app.helper.RxFunctions
import com.classroomjoin.app.helper_utils.Interactor
import rx.Observable
import rx.Observer
import java.text.SimpleDateFormat
import java.util.*


class InputStudentViewModel
    internal constructor(context: Context, private val presenter: InputStudentCodePresenter)
        : Interactor(context) {

    fun postData(addStudentModel: InputCodeModel) {
        addStudentModel.userTypeId=user_type
        getObservable(addStudentModel).subscribe(Observer)
    }

    private fun getObservable(addStudentModel: InputCodeModel):Observable<InputStudentCodeResponse>{
        addStudentModel.userId=teacher_id.toInt()
        addStudentModel.createDate=getCurrentDate()
        return apiInterface.connectStudent(accesstoken,addStudentModel).compose(RxFunctions.applySchedulers())
    }

    private val Observer:Observer<InputStudentCodeResponse>
             get() = object :Observer<InputStudentCodeResponse>{
                 override fun onCompleted() {
                 }

                 override fun onError(e: Throwable?) {
                     presenter.serverError()
                     e?.printStackTrace()
                 }

                 override fun onNext(t: InputStudentCodeResponse?) {
                     if(t?.status=="Success"){
                         presenter.postsuccess()
                     }
                     else presenter.setError(t!!.error_message)
                 }
             }

    fun connect_students():Int{
        if(user_type==4)return 2 else return 1
    }

    private fun getCurrentDate(): String {
        val c = Calendar.getInstance()
        val df = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        return df.format(c.getTime())
    }
}
