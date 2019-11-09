package com.classroomjoin.app.classEditPage


import com.google.gson.annotations.SerializedName


class AddClassResponse {
    @SerializedName("status")
    var status: String? = null
    @SerializedName("error_message")
    var error_message: String? = null
    @SerializedName("error_code")
    var error_code: String? = null
}
