package com.classroomjoin.app.loginPage.signupGoogle

import com.google.gson.annotations.SerializedName

data class SignInGoogleReponser(
        @SerializedName("status") val status: String,
        @SerializedName("data") val data: SignInGoogleDataReponser,
        @SerializedName("errormessage") val errormessage: String,
        @SerializedName("errorcode") val errorcode: String)



