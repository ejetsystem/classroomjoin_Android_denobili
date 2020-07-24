package com.denobili.app.sign_step.signup_manager

import com.google.gson.annotations.SerializedName

class SignApiResponse {
    @SerializedName("code")
    var code: String? = null

    @SerializedName("data")
    var data: String? = null

    @SerializedName("dataStatus")
    var dataStatus: String = "null"
    @SerializedName("message")
    var message: String = "null"
    @SerializedName("status")
    var status: String = "null"

}