package com.denobili.app.addSocialGrade

import android.content.Context
import com.denobili.app.helper.RxFunctions
import com.denobili.app.helper_utils.Interactor
import com.denobili.app.mySocialGradesPage.AddSocialGradeModel
import com.denobili.app.mySocialGradesPage.DeleteSocialModel
import com.denobili.app.mySocialGradesPage.MySocialGradesModel
import rx.Observable
import java.text.SimpleDateFormat
import java.util.*


class AddSocialGradeViewModel
internal constructor(context: Context)
    : Interactor(context) {

    fun deleteSocialGrade(social_id: String?): Observable<AddSocialGradeResponse> =
            getdeleteObservable(DeleteSocialModel(account_id,social_id,"0",getCurrentDate()))

    fun updateSocialGrade(addSocialGradeModel: MySocialGradesModel): Observable<AddSocialGradeResponse> =
            geteditObservable(addSocialGradeModel)

    fun addSocialGrade(addSocialGradeModel: AddSocialGradeModel): Observable<AddSocialGradeResponse> =
            getObservable(addSocialGradeModel)

    private fun getObservable(addSocialGradeModel: AddSocialGradeModel): Observable<AddSocialGradeResponse> {
        addSocialGradeModel.orgId = org_id
        addSocialGradeModel.accountManagementId = account_id
        return apiInterface.addSocialgrade(accesstoken, addSocialGradeModel).compose(RxFunctions.applySchedulers())
    }


    private fun getdeleteObservable(addSocialGradeModel: DeleteSocialModel): Observable<AddSocialGradeResponse> {
        return apiInterface.deleteSocialgrade(accesstoken, addSocialGradeModel
               ).compose(RxFunctions.applySchedulers())
    }

    private fun geteditObservable(addSocialGradeModel: MySocialGradesModel): Observable<AddSocialGradeResponse> {
        return apiInterface.editSocialgrade(accesstoken, addSocialGradeModel.id!!,
                addSocialGradeModel.name, addSocialGradeModel.point!!,
                addSocialGradeModel.type, org_id.toInt(), account_id.toInt(),
                getCurrentDate()).compose(RxFunctions.applySchedulers())
    }

    private fun getCurrentDate(): String {
        val c = Calendar.getInstance()
        val df = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        return df.format(c.time)
    }
}
