package com.classroomjoin.app.manageAccountsPackage

import android.content.Context
import com.classroomjoin.app.helper_utils.DisplayItem
import com.classroomjoin.app.helper_utils.Interactor
import com.classroomjoin.app.homepage.HomePage
import com.classroomjoin.app.loginPage.Userdata
import com.classroomjoin.app.myConnectedStudents.MyConnectedStudentsListingActivity
import com.vicpin.krealmextensions.allItemsAsObservable
import org.jetbrains.anko.startActivity
import rx.Observable
import rx.Observer
import java.util.*


class ManageAccountsInteractor
internal constructor(private val context: Context, private val presenter: ManageAccountPresenter)
    : Interactor(context) {

    fun getData() {
        getObservable().subscribe(Observer)
    }

    private fun getObservable(): Observable<List<Userdata>> = Userdata().allItemsAsObservable()

    private val Observer: Observer<List<Userdata>>
        get() = object : Observer<List<Userdata>> {
            override fun onCompleted() {
            }

            override fun onError(e: Throwable?) {
                presenter.serverError()
                e?.printStackTrace()
            }

            override fun onNext(t: List<Userdata>?) {
                val list = ArrayList<DisplayItem>()
                list.addAll(t!!)
                presenter.setData(list)
            }
        }

    fun setUser(userdata: Userdata) {
        userid = userdata.user_id.toString()
        account_id = userdata.acc_id.toString()
        org_id = userdata.orgid.toString()
        teacher_id = userdata.teacherid.toString()
        user_type = userdata.usertype_id!!
        user_email = userdata.email
       // user_name = userdata.firstname
        profile_url = userdata.profile_url

        if (!userdata.firstname.isNullOrBlank())
            user_name = userdata.firstname

        if (!userdata.middlename.isNullOrBlank())
            user_name = user_name + " " + userdata.middlename

        if (!userdata.lastname.isNullOrBlank())
            user_name = user_name + " " + userdata.lastname

        if (userdata.admin_id != null) {
            admin_id = userdata.admin_id.toString()
            isAdmin = (user_type == 3)
        }

        if (user_type == 1 || user_type == 2 || user_type == 3)
            context.startActivity<HomePage>()
        else context.startActivity<MyConnectedStudentsListingActivity>()

    }
}
