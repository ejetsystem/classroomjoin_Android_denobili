package com.denobili.app.attendanceSummaryPage

import com.google.gson.annotations.SerializedName


data class AttendanceSummaryResponse(@SerializedName("status")
                                     val status: String,
                                     @SerializedName("error_response")
                                     val error_response: String,
                                     @SerializedName("data")
                                     val data: List<AttendanceSummaryRow>,
                                     @SerializedName("error_code")
                                     val error_code: String)