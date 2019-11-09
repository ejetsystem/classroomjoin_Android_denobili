package com.classroomjoin.app.addSocialGrade

import com.google.gson.annotations.SerializedName


class AddSocialGradeResponse(
        @SerializedName("status")
        val status: String,
        @SerializedName("error_response")
        val error_response: String,
        @SerializedName("error_code")
        val error_code: String)
