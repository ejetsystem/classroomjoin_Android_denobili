package com.classroomjoin.app.addTemplatePage

import com.google.gson.annotations.SerializedName


data class TemplateResponse(@SerializedName("status") val status: String,
                            @SerializedName("error_message") val error_message: String)