package com.classroomjoin.app.loginPage.loiginMicroService

import com.google.gson.annotations.SerializedName

data class LoginMicroDataReponser(
        @SerializedName("emailId") val emailId: String,
        @SerializedName("mobileNo") val mobileNo: String,
        @SerializedName("registrationTypeId") val registrationTypeId: Int,
        @SerializedName("userTypeId") val userTypeId: Int,
        @SerializedName("activeFlag") val activeFlag: Int,
        @SerializedName("createDate") val createDate: String,
        @SerializedName("googleToken") val googleToken: String)

