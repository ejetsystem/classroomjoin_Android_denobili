package com.denobili.app.classEditPage

import android.content.Context
import com.denobili.app.R
import com.denobili.app.helper.RxFunctions
import com.denobili.app.helper_utils.Interactor
import com.denobili.app.teacherAddClasses.ClassListModel
import com.denobili.app.teacherOutboxPage.OutboxModel
import com.vicpin.krealmextensions.delete
import com.vicpin.krealmextensions.query
import rx.Observable
import rx.Observer
import java.text.SimpleDateFormat
import java.util.*

class EditClassInteractor
internal constructor(private var context: Context, private val presenter: EditClassPresenter)
    : Interactor(context) {


    fun addClass(addClassModel: AddClassModel) {
        addClassModel.acc_id = account_id.toInt()

        if (addClassModel.class_id == null)
            getAddObservable(addClassModel).subscribe(addObserver)
        else
            getEditObservable(addClassModel).subscribe(editObserver)
    }

    private fun getAddObservable(addClassModel: AddClassModel): Observable<AddClassResponse> {
        addClassModel.org_id = org_id
        addClassModel.acc_id = account_id.toInt()
        addClassModel.createDate = getCurrentDate()
        return apiInterface.addClass(accesstoken, addClassModel).compose(RxFunctions.applySchedulers())
    }

    private fun getEditObservable(addClassModel: AddClassModel): Observable<AddClassResponse> =
            apiInterface.editClass(accesstoken, addClassModel.name, addClassModel.class_id,getCurrentDate(),addClassModel.acc_id).compose(RxFunctions.applySchedulers())

    private val addObserver: Observer<AddClassResponse>
        get() = object : Observer<AddClassResponse> {
            override fun onCompleted() {
            }

            override fun onError(e: Throwable?) {
                presenter.postfailure(context.getString(R.string.some_error))
                e?.printStackTrace()
            }

            override fun onNext(t: AddClassResponse?) {
                if (t?.status == "Success") {
                    presenter.postsuccess()
                } else if (t != null) {
                    presenter.postfailure(t.error_message!!)
                }

            }
        }

    private val editObserver: Observer<AddClassResponse>
        get() = object : Observer<AddClassResponse> {
            override fun onCompleted() {
            }

            override fun onError(e: Throwable?) {
                presenter.postfailure(context.getString(R.string.some_error))
                e?.printStackTrace()
            }

            override fun onNext(t: AddClassResponse?) {
                if (t?.status == "Success") {
                    presenter.postsuccess()
                } else if (t != null) {
                    presenter.postfailure(t.error_message!!)
                }

            }
        }

    fun deleteclass(class_id: String?) {
        val list = OutboxModel().query { query -> query.equalTo("account_id", account_id) }
        if (list.isEmpty()) apiInterface.deleteClass(accesstoken, class_id).compose(RxFunctions.applySchedulers()).subscribe(deleteObserver(class_id))
        else presenter.deletionFailure(context.getString(R.string.unsend_outbox_items))
    }

    private fun deleteObserver(class_id: String?): Observer<DeleteClassResponse> {
        return object : Observer<DeleteClassResponse> {
            override fun onNext(t: DeleteClassResponse?) {
                if (t?.status == "Success") {
                    try {
                        ClassListModel().delete { realmQuery -> realmQuery.equalTo("id", class_id!!.toInt()) }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                    presenter.deleteditem()
                } else
                    presenter.deletionFailure(t!!.error_message)
            }

            override fun onError(e: Throwable?) {
                presenter.deletionFailure(context.getString(R.string.some_error))
            }

            override fun onCompleted() {
            }
        }
    }

    private fun getCurrentDate(): String {
        val c = Calendar.getInstance()
        val df = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        return df.format(c.time)
    }


}
