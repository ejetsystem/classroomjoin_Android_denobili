package com.classroomjoin.app.postAttendancePage.checkAttendance


import com.google.gson.annotations.SerializedName

class AttendanceMicroApiResponse {
    @SerializedName("status")
    var status: String? = null
    @SerializedName("error_code")
    var error_code: String? = null
    @SerializedName("message")
    var error_message: String? = null
    @SerializedName("data")
    var data: Boolean? = null


}
