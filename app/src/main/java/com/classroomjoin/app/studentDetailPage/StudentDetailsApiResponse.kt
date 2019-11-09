package com.classroomjoin.app.studentDetailPage

import com.google.gson.annotations.SerializedName


class StudentDetailsApiResponse(
        @SerializedName("status")
        val status: String,
        @SerializedName("error_code")
        val error_code: String,
        @SerializedName("error_message")
        val error_message: String,
        @SerializedName("data")
        val student: StudentDetail)