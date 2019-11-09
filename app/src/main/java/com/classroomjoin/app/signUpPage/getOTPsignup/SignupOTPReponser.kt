package com.classroomjoin.app.signUpPage.getOTPsignup

import com.google.gson.annotations.SerializedName

data class SignupOTPReponser(
        @SerializedName("status") val status: String,
        @SerializedName("data") val data: SignupOTPDataReponser,
        @SerializedName("errormessage") val errormessage: String,
        @SerializedName("errorcode") val errorcode: String)



