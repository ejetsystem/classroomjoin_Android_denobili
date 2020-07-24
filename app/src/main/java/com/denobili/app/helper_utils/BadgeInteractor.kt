package com.denobili.app.helper_utils

import android.content.Context
import com.denobili.app.R
import com.denobili.app.helper.RxFunctions
import com.denobili.app.studentDetailPage.AddStudentModel
import com.denobili.app.studentEventDetailPage.StudentEventApiResponse
import com.denobili.app.studentallEventsPage.StudentEventModel
import com.vicpin.krealmextensions.createOrUpdate
import com.vicpin.krealmextensions.query
import org.jetbrains.anko.error
import rx.Observable
import rx.Observer

class BadgeInteractor internal constructor(private val context: Context, private val presenter: BadgePresenter)
    : Interactor(context) {
    fun getdata() {
        getObservable(student_id).subscribe(detailsObserver)
    }
    private fun getObservable(studentid: String): Observable<BadgeCountResponce> {
        return apiInterface.badge_details(accesstoken, studentid.toInt()).compose(RxFunctions.applySchedulers())
    }

    private val detailsObserver: Observer<BadgeCountResponce>
        get() = object : Observer<BadgeCountResponce> {

            override fun onNext(t: BadgeCountResponce?) {
                if (t?.status == "Success") {
                    presenter.postStudentdetails(t.data)
                } else presenter.noresult("error")
            }

            override fun onError(e: Throwable?) {
               // presenter.setError(context.getString(R.string.some_error))
                e?.printStackTrace()
            }

            override fun onCompleted() {
            }
        }
}
