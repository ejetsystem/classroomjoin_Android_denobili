package com.denobili.app.profileEditPage

import com.google.gson.annotations.SerializedName


data class ProfilePostReponse(
        @SerializedName("status") val status: String,
        @SerializedName("error_message") val error_message: String,
        @SerializedName("error_code") val error_code: String)
