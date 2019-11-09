package com.classroomjoin.app.profilePage

import com.google.gson.annotations.SerializedName


data class ProfileApiResponse(
        @SerializedName("status") val status: String,
        @SerializedName("error_message") val error_message: String,
        @SerializedName("error_code") val error_code: String,
        @SerializedName("data") val data: ProfileData)
