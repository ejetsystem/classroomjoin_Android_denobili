package com.classroomjoin.app.studentCodePage

import com.google.gson.annotations.SerializedName


class InputStudentCodeResponse(
        @SerializedName("status")
        val status:String,
        @SerializedName("errorcode")
        val error_code:String,
        @SerializedName("data")
        val data: String,
        @SerializedName("errormessage")
        val error_message:String)
