package com.classroomjoin.app.loginPage.loiginMicroService

import com.google.gson.annotations.SerializedName

data class LoginMicroReponser(
        @SerializedName("status") val status: String,
        @SerializedName("data") val data: LoginMicroDataReponser,
        @SerializedName("errormessage") val errormessage: String,
        @SerializedName("errorcode") val errorcode: String)



