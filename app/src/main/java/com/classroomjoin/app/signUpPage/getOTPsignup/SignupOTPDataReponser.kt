package com.classroomjoin.app.signUpPage.getOTPsignup

import com.google.gson.annotations.SerializedName

data class SignupOTPDataReponser(
        @SerializedName("emailId")val emailId:String,
        @SerializedName("mobileNo")val mobileNo:String,
        @SerializedName("otp")val otp:String)
