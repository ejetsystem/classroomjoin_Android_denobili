package com.classroomjoin.app.attendancePage


import com.google.gson.annotations.SerializedName

class AttendanceApiResponse {
    @SerializedName("status")
    val status: String? = null
    @SerializedName("error_code")
    val error_code: String? = null
    @SerializedName("error_message")
    val error_message: String? = null
    @SerializedName("data")
    val data: List<AttendanceData>? = null
}
