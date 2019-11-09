package com.classroomjoin.app.loginPage


import com.google.gson.annotations.SerializedName

class LoginApiResponse {
    @SerializedName("status")
    var status: String? = null
    @SerializedName("error_code")
    var error_code: String? = null
    @SerializedName("message")
    var error_message: String? = null
    @SerializedName("data")
    var userdata: Userdata = Userdata()


}
