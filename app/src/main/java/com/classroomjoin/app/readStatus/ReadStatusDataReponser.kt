package com.classroomjoin.app.readStatus

import com.google.gson.annotations.SerializedName

data class ReadStatusDataReponser(
        @SerializedName("emailId") val emailId: String,
        @SerializedName("mobileNo") val mobileNo: String,
        @SerializedName("otp") val otp: String)
