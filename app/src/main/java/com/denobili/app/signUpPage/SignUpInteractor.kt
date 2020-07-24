package com.denobili.app.signUpPage

import android.content.Context
import com.denobili.app.helper.RxFunctions
import com.denobili.app.helper_utils.Interactor
import rx.Observable
import rx.Observer


class SignUpInteractor
    internal constructor(context: Context, private val presenter: SignUpPresenter)
        : Interactor(context) {

    fun postData(signUpModel: SignUpModel) {
        getObservable(signUpModel).subscribe(Observer)
    }

    private fun getObservable(signUpModel: SignUpModel):Observable<SignUpApiResponse>{
        return apiInterface.signup(signUpModel).compose(RxFunctions.applySchedulers())
    }

    private val Observer:Observer<SignUpApiResponse>
             get() = object :Observer<SignUpApiResponse>{
                 override fun onCompleted() {
                 }

                 override fun onError(e: Throwable?) {
                     presenter.serverError()
                     e?.printStackTrace()
                 }

                 override fun onNext(t: SignUpApiResponse) {
                     if(t?.status=="Success"){
                         presenter.postsuccess()
                     }
                     else presenter.setError(t!!.error_message)

                 }
             }
}
