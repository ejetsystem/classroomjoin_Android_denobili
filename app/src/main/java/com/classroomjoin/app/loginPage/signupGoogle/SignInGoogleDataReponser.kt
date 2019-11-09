package com.classroomjoin.app.loginPage.signupGoogle

import com.google.gson.annotations.SerializedName

data class SignInGoogleDataReponser(
        @SerializedName("emailId") val emailId: String,
        @SerializedName("firstName") val firstName: String,
        @SerializedName("mobileNo") val mobileNo: String,
        @SerializedName("registrationTypeId") val registrationTypeId: Int,
        @SerializedName("userTypeId") val userTypeId: Int,
        @SerializedName("socialId") val socialId: Int,
        @SerializedName("createDate") val createDate: String)

