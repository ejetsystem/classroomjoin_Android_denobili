package com.classroomjoin.app.signUpPage


import com.google.gson.annotations.SerializedName

class SignUpApiResponse {

    @SerializedName("status")
    var status: String? = null

    @SerializedName("error_code")
    var error_code: String? = null

    @SerializedName("errormessage")
    var error_message: String = "null"


}
