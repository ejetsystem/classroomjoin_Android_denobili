package com.denobili.app.teacherMessageSendPage

import android.content.Context
import com.denobili.app.helper.RxFunctions
import com.denobili.app.helper_utils.Interactor
import rx.Observable


class MessagingApiService internal constructor(context: Context)
    : Interactor(context) {

    fun postMail(model: EmailSendModel): Observable<SendModelResponse> {
        model.acc_id = account_id
        model.orgid = org_id
        return apiInterface.sendMail(accesstoken, model).compose(RxFunctions.applySchedulers())
    }

    fun postDiary(model: DiarySendModel) : Observable<SendModelResponse> {
        model.acc_id=account_id
        model.orgid=org_id
        return apiInterface.sendDiary(accesstoken,model).compose(RxFunctions.applySchedulers())
    }

    fun postNotification(model: NotifySendModel):Observable<SendModelResponse> {
        model.acc_id = account_id
        model.orgid = org_id
        return apiInterface.sendNotification(accesstoken, model).compose(RxFunctions.applySchedulers())
    }

    fun postSms(model: SmsSendModel):Observable<SendModelResponse> {
        model.acc_id=account_id
        model.orgid=org_id
        return apiInterface.sendSms(accesstoken,model).compose(RxFunctions.applySchedulers())
    }

    fun postAlternate(model: AlternateSendModel):Observable<SendModelResponse> {
        model.acc_id=account_id
        model.orgid=org_id
        return apiInterface.sendAltername(accesstoken,model).compose(RxFunctions.applySchedulers())
    }

}