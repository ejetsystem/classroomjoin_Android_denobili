package com.classroomjoin.app.addTemplatePage

import android.content.Context
import com.classroomjoin.app.helper.RxFunctions
import com.classroomjoin.app.helper_utils.Interactor
import com.classroomjoin.app.templatePage.Template
import com.vicpin.krealmextensions.createOrUpdate
import com.vicpin.krealmextensions.delete
import com.vicpin.krealmextensions.queryAsObservable
import rx.Observable
import java.text.SimpleDateFormat
import java.util.*


class TemplateViewModel
internal constructor(context: Context)
    : Interactor(context) {

    fun addTemplate(item: AddTemplateModel)
            : Observable<TemplateResponse> =
            apiInterface.addTemplate(accesstoken, item)
                    .compose(RxFunctions.applySchedulers())

    fun getTemplate(id: Int, type_id: Int): Observable<List<Template>>
            = Template().queryAsObservable { realmQuery ->
        realmQuery.equalTo("id", id).equalTo("type_id", type_id)
    }.first()

    fun editTemplate(addTemplateModel: Template)
            : Observable<TemplateResponse> =
            apiInterface.editTemplate(accesstoken,
                    addTemplateModel.id,
                    addTemplateModel.subject,
                    addTemplateModel.message,
                    addTemplateModel.type_id,
                    addTemplateModel.account_id, getCurrentDate())
                    .compose(RxFunctions.applySchedulers())

    fun deleteTemplate(id: Int): Observable<TemplateResponse> =
            apiInterface.deleteTemplate(accesstoken, id).compose(RxFunctions.applySchedulers())

    fun deleteItemFromDatabase(id: Int, type_id: Int) {
        Template().delete { realmQuery ->
            realmQuery.equalTo("id", id).equalTo("type_id", type_id)
        }
    }

    fun updateItem(item: Template) {
        item.createOrUpdate()
    }

    private fun getCurrentDate(): String {
        val c = Calendar.getInstance()
        val df = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        return df.format(c.time)
    }

}
