package com.classroomjoin.app.passwordConfirmation

import com.google.gson.annotations.SerializedName


data class ConfirmPassReponser(
        @SerializedName("status")val status:String,
        @SerializedName("data")val data:String,
        @SerializedName("error_message")val error:String,
        @SerializedName("error_code")val error_code:String)