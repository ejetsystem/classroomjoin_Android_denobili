package com.classroomjoin.app.teacherMessageSendPage

import com.google.gson.annotations.SerializedName


data class SendModelResponse(@SerializedName("status")
                             val status: String,
                             @SerializedName("errorcode")
                             val error_code: String,
                             @SerializedName("data")
                             val data: String,
                             @SerializedName("errormessage")
                             val error_message: String)