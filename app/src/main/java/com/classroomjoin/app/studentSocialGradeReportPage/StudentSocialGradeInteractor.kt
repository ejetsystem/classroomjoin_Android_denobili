package com.classroomjoin.app.studentSocialGradeReportPage

import android.content.Context
import com.classroomjoin.app.helper.RxFunctions
import com.classroomjoin.app.helper_utils.DisplayItem
import com.classroomjoin.app.helper_utils.Interactor
import org.jetbrains.anko.AnkoLogger
import rx.Observer


class StudentSocialGradeInteractor
    internal constructor(context: Context, private val presenter: StudentSocialGradePresenter)
        : Interactor(context),AnkoLogger {

    fun getData(month:String,year:String) {
      apiInterface.getSocialGradeStudent(accesstoken,student_id,month,year).compose(RxFunctions.applySchedulers()).subscribe(Observer)
    }

    private val Observer:Observer<SocialGradeReportResponse>
        get() = object :Observer<SocialGradeReportResponse>{
            override fun onCompleted() {
            }

            override fun onError(e: Throwable?) {
                presenter.serverError()
                e?.printStackTrace()
            }

            override fun onNext(t: SocialGradeReportResponse?) {
                if(t?.status=="Success"){
                    val list= java.util.ArrayList<DisplayItem>()

                    if(t.data.totalGrade>0.0f) {
                        val model = SocialGradeChartModel(t.data.positive, t.data.negative)
                        list.add(model)
                        list.addAll(t.data.data)
                    }
                    presenter.setData(list)

                }
                else presenter.setError(t!!.error_message)

            }
        }


}
