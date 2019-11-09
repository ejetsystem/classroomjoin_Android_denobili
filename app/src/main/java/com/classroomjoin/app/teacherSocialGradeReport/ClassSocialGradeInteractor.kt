package com.classroomjoin.app.teacherSocialGradeReport

import android.content.Context
import com.classroomjoin.app.helper.RxFunctions
import com.classroomjoin.app.helper_utils.DisplayItem
import com.classroomjoin.app.helper_utils.Interactor
import com.classroomjoin.app.studentSocialGradeReportPage.SocialGradeChartModel
import org.jetbrains.anko.AnkoLogger
import rx.Observer


class ClassSocialGradeInteractor
internal constructor(context: Context, private val presenter: ClassSocialGradePresenter)
    : Interactor(context), AnkoLogger {

    fun getData(classid: String) {
        apiInterface.getSocialGradeReport(accesstoken, classid).compose(RxFunctions.applySchedulers()).subscribe(Observer)
    }

    private val Observer: Observer<SocialGradeClassReportResponse>
        get() = object : Observer<SocialGradeClassReportResponse> {
            override fun onCompleted() {
            }

            override fun onError(e: Throwable?) {
                presenter.serverError()
                e?.printStackTrace()
            }

            override fun onNext(t: SocialGradeClassReportResponse?) {
                if (t?.status == "Success") {
                    val list = java.util.ArrayList<DisplayItem>()
                    val model = SocialGradeChartModel(t.data.positive, t.data.negative)
                    list.add(model)
                    list.addAll(t.data.data)
                    presenter.setData(list)
                } else presenter.setError(t!!.error_message)

            }
        }


}
