package com.denobili.app.sign_step.signup_manager

import android.content.Context
import com.denobili.app.helper.RxFunctions
import com.denobili.app.helper_utils.Interactor
import rx.Observable
import rx.Observer

class SignInteractor internal constructor(context: Context, private val presenter: SignPresenter)
    : Interactor(context) {

    fun postData(signUpModel: SignupMobileModel) {
        getObservable(signUpModel).subscribe(Observer)
    }

    private fun getObservable(signUpModel: SignupMobileModel): Observable<SignApiResponse> {
        return apiInterface.userRegister(signUpModel).compose(RxFunctions.applySchedulers())
    }

    private val Observer: Observer<SignApiResponse>
        get() = object : Observer<SignApiResponse> {
            override fun onCompleted() {
            }

            override fun onError(e: Throwable?) {
                presenter.serverError()
                e?.printStackTrace()
            }

            override fun onNext(t: SignApiResponse) {
                if(t?.status=="Success"){
                    presenter.postsuccess()
                }
                else presenter.setError(t!!.message)

            }
        }
}
