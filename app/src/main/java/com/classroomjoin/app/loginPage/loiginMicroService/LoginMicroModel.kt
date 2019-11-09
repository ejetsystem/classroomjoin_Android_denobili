package com.classroomjoin.app.loginPage.loiginMicroService

import com.google.gson.annotations.SerializedName

class LoginMicroModel {

    @SerializedName("emailId")
    var emailId: String? = null

    @SerializedName("mobileNo")
    var mobileNo: String? = null


    constructor(userName11: String?, updateDate11: String?) {
        this.emailId = userName11
        this.mobileNo = updateDate11
    }

    constructor(userName11: String?) {
        this.emailId = userName11
    }

}