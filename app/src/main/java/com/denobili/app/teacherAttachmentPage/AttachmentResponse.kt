package com.denobili.app.teacherAttachmentPage

import com.google.gson.annotations.SerializedName


data class AttachmentResponse(
        @SerializedName("status") var status: String,
        @SerializedName("data") var data: String,
        @SerializedName("error_message") var error: String)


