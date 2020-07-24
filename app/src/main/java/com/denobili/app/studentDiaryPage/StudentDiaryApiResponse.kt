package com.denobili.app.studentDiaryPage
import com.google.gson.annotations.SerializedName

data class StudentDiaryApiResponse(
        @SerializedName("status")var status:String,
        @SerializedName("data")var data:StudentDiaryContentModel,
        @SerializedName("error_code")var error_code:String?=null,
        @SerializedName("error_message")var error_status:String?=null)