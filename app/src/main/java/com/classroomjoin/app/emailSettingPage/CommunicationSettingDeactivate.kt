package com.classroomjoin.app.emailSettingPage

import com.google.gson.annotations.SerializedName


data class CommunicationSettingDeactivate(
        @SerializedName("status") val status: String,
        @SerializedName("error_message") val error_message: String)