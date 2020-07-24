package com.denobili.app.studentSocialGradeReportPage

import com.google.gson.annotations.SerializedName


data class SocialGradeReportResponse(@SerializedName("status") val status: String,
                                     @SerializedName("data") val data: SocialGradeModel,
                                     @SerializedName("error_code") val error_code: String,
                                     @SerializedName("error_message") val error_message: String)