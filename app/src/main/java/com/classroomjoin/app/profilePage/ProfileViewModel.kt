package com.classroomjoin.app.profilePage

import android.content.Context
import com.classroomjoin.app.R
import com.classroomjoin.app.helper.RxFunctions
import com.classroomjoin.app.helper_utils.Interactor
import com.classroomjoin.app.profileEditPage.ProfilePostReponse
import rx.Observable
import rx.android.schedulers.AndroidSchedulers


class ProfileViewModel internal constructor(private var context: Context)
    : Interactor(context) {


    fun getData(userid: String): Observable<ProfileApiResponse>? {
        return apiInterface.personal_details(accesstoken, userid.toInt())
                .compose(RxFunctions.applySchedulers())
    }

    private fun getResult(): Observable.Transformer<ProfileAction, ProfileResult> {
        val results: Observable.Transformer<ProfileAction, ProfileResult> = Observable.Transformer { profileAction ->
            profileAction.flatMap { (user_id) -> getData(user_id) }
                    .map { t: ProfileApiResponse? ->
                        if (t!!.status == "Success") {
                            onSucces(t.data)
                        } else {
                            onFailure(t.error_message)
                        }
                    }
                    .onErrorReturn { t: Throwable? ->
                        onFailure(context.getString(R.string.serverError))
                    }
                    .observeOn(AndroidSchedulers.mainThread())
                    .startWith(inFlight())
        }
        return results
    }

    fun postAction(): Observable<ProfileResult> =
            Observable.just(ProfileAction(userid)).compose(getResult())

    fun getProfileUiModel(): Observable<ProfileUIModel> {
        return postAction().map { t: ProfileResult ->
            when (t) {
                is onSucces -> converterSuccess(t.data)
                is onFailure -> Failure(t.error)
                is inFlight -> inProgress()
            }
        }
    }

    private fun converterSuccess(data: ProfileData): Success {
        return Success(getUserName(data), data.gnder, data.dob, data.imageUrl,
                getContactDetails(data), data.mobileNo, data.emailId)
    }

    private fun getPersonalDetails(data: ProfileData): String {
        val builder = StringBuilder()
        user_name = data.firstName + " " + data.middleName + " " + data.lastName
        user_email = data.emailId
        builder.append(user_name).appendln().append(user_email).appendln()
        if (data.gnder != null) builder.append(context.getString(R.string.gender)).append(" ").append(data.gnder).appendln()
        if (data.dob != null) builder.append(context.getString(R.string.dob)).append(" ").append(data.dob).appendln()
        return builder.toString()

    }

    private fun getUserName(data: ProfileData): String {
        user_email = data.emailId
        if (!data.firstName.isNullOrBlank())
            user_name = data.firstName

        if (!data.middleName.isNullOrBlank())
            user_name = user_name + " " + data.middleName

        if (!data.lastName.isNullOrBlank())
            user_name = user_name + " " + data.lastName
        //user_name=data.firstName+" "+data.middleName+" "+data.lastName
        return user_name

    }

    private fun getContactDetails(data: ProfileData): String {
        val builder = StringBuilder()
        if (!data.add_1.isNullOrEmpty()) builder.add(data.add_1).appendln()
        if (!data.add_2.isNullOrEmpty()) builder.add(data.add_2).appendln()
        if (!data.city.isNullOrEmpty()) builder.add(data.city).append(", ")
        if (!data.state.isNullOrEmpty()) builder.add(data.state).append(", ")
        if (!data.country.isNullOrEmpty()) builder.add(data.country).append(" ")
        builder.add("")
        return builder.toString()
    }

    fun StringBuilder.add(string: String?): StringBuilder {
        if (string != null) this.append(string)
        return this
    }

    fun postData(userInput: ProfileData): Observable<ProfilePostReponse> =
            apiInterface.updateProfileData(accesstoken, userInput).compose(RxFunctions.applySchedulers())


}