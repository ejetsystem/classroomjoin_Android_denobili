package com.denobili.app.postAttendancePage


import com.google.gson.annotations.SerializedName


class PostAttendanceResponse {

    @SerializedName("status")
    var status: String? = null

    @SerializedName("errormessage")
    var error_message: String? = null

    @SerializedName("error_code")
    var error_code: String? = null
}
