package com.denobili.app.classEditPage

import com.google.gson.annotations.SerializedName


data class DeleteClassResponse(
        @SerializedName("status")
        val status: String,
        @SerializedName("error_code")
        val error_code: String,
        @SerializedName("error_message")
        val error_message: String)
