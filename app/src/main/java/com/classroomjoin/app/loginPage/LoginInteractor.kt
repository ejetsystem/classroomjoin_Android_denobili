package com.classroomjoin.app.loginPage

import android.content.Context
import com.classroomjoin.app.helper.RxFunctions
import com.classroomjoin.app.helper_utils.Interactor
import com.classroomjoin.app.homepage.HomePage
import com.classroomjoin.app.myConnectedStudents.MyConnectedStudentsListingActivity
import com.classroomjoin.app.signUpPage.SignUpApiResponse
import com.classroomjoin.app.signUpPage.SignUpModel
import com.crashlytics.android.Crashlytics
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.vicpin.krealmextensions.save
import org.jetbrains.anko.startActivity
import retrofit2.adapter.rxjava.Result
import rx.Observable
import rx.Observer


class LoginInteractor
internal constructor(private val context: Context, private val presenter: LoginPresenter)
    : Interactor(context) {

    private var socialid: String? = null

    fun postData(loginModel: LoginModel, change_account: Boolean) {
        getObservable(loginModel).subscribe(Observer(change_account))
    }


    private fun getObservable(loginModel: LoginModel): Observable<Result<LoginApiResponse>> {
        return apiInterface.login(loginModel).compose(RxFunctions.applySchedulers())
    }


    private fun Observer(change_account: Boolean): Observer<Result<LoginApiResponse>> {
        return object : Observer<Result<LoginApiResponse>> {
            override fun onCompleted() {
            }

            override fun onError(e: Throwable?) {
                presenter.serverError()
                e?.printStackTrace()
            }

            override fun onNext(t: Result<LoginApiResponse>?) {

                /* println("Data--->"+"onNext-->"+t!!.error())
                 println("Data--->"+"onNext-->"+t!!.isError())
                 println("Data--->"+"onNext-->"+t!!.response())

                 presenter.postsuccess()*/

                if (t?.response()!!.isSuccessful) {
                    val loginResponse: LoginApiResponse? = t.response().body()
                    if (loginResponse?.status == "Success") {
                        try {
                            accesstoken = t.response().headers().get("Authorization")

                            val listType = object : TypeToken<List<AccountManagementBean>>() {}.getType()
                            val posts = Gson().fromJson(loginResponse.userdata.accountManagementBean!!.toString(), listType) as List<AccountManagementBean>

                            if (posts.isNotEmpty()) {
                                for (item: AccountManagementBean in posts) {
                                    item.userId = loginResponse.userdata.user_id
                                    item.save()
                                }

                                loginResponse.userdata.acc_id = posts.get(0).accountManagementId

                            }
                            val listType_teacher = object : TypeToken<List<TeacherLoginBean>>() {}.getType()
                            val posts_teacher = Gson().fromJson(loginResponse.userdata.teacherLoginBean!!.toString(), listType_teacher) as List<TeacherLoginBean>

                            if (posts_teacher.isNotEmpty()) {

                                for (item: TeacherLoginBean in posts_teacher) {
                                    item.userId = loginResponse.userdata.user_id
                                    item.save()
                                }

                                loginResponse.userdata.teacherid = posts_teacher.get(0).teacherId
                                loginResponse.userdata.admin_id = posts_teacher.get(0).teachersAdminId

                            }

                            if (loginResponse.userdata.usertype_id == 5 || loginResponse.userdata.usertype_id == 4) {
                                loginResponse.userdata.teacherid = loginResponse.userdata.user_id

                            }
                            loginResponse.userdata.save()
                            saveData(loginResponse.userdata)


                            presenter.postsuccess()
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }

                    } else {
                        presenter.setError(loginResponse?.error_message)
                    }

                } else {
                    println("Data--->" + "serverError-->" + t!!.response().toString())
                    println("Data--->" + "serverError-->" + t!!.response().errorBody().toString())

                    //presenter.serverError()

                    presenter.setError("The password you entered is incorrect. Please try again.")


                }
            }
        }
    }

    fun postSignup(signUpModel: SignUpModel, change_account: Boolean) {
        getSignupObserver(signUpModel).subscribe(signUpObserver(change_account))
    }

    private fun getSignupObserver(signUpModel: SignUpModel): Observable<SignUpApiResponse> {
        signUpModel.register_type = "2"
        // socialid=signUpModel.socialId
        return apiInterface.signup(signUpModel).compose(RxFunctions.applySchedulers())
    }

    private fun signUpObserver(change_account: Boolean): Observer<SignUpApiResponse> {
        return object : Observer<SignUpApiResponse> {
            override fun onCompleted() {
            }

            override fun onError(e: Throwable?) {
                presenter.serverError()
                e?.printStackTrace()
            }

            override fun onNext(t: SignUpApiResponse?) {
                if (t?.status == "Success") {
                    postData(LoginModel(socialid, socialid), change_account)
                } else presenter.setError(t!!.error_message)
            }
        }
    }

    private fun saveData(userdata: Userdata) {
        userid = userdata.user_id.toString()
        account_id = userdata.acc_id.toString()
        org_id = userdata.orgid.toString()
        teacher_id = userdata.teacherid.toString()
        user_type = userdata.usertype_id!!
        user_email = userdata.email
        user_mobileNo = userdata.mobileNo

        if (!userdata.firstname.isNullOrBlank())
            user_name = userdata.firstname

        if (!userdata.middlename.isNullOrBlank())
            user_name = user_name + " " + userdata.middlename

        if (!userdata.lastname.isNullOrBlank())
            user_name = user_name + " " + userdata.lastname


        Crashlytics.setUserIdentifier(userid)
        Crashlytics.setUserName(user_name)
        Crashlytics.setUserEmail(user_email)
        if (userdata.mobileNo != null)
            Crashlytics.setString("Mobile No:", userdata.mobileNo)

        profile_url = userdata.profile_url

        if (userdata.admin_id != null) {
            admin_id = userdata.admin_id.toString()
            isAdmin = (user_type == 3)
        }

        if (user_type == 1 || user_type == 2 || user_type == 3)
            context.startActivity<HomePage>()
        else
            context.startActivity<MyConnectedStudentsListingActivity>()

    }

}
