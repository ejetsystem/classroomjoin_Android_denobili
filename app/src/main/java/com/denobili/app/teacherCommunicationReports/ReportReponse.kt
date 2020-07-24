package com.denobili.app.teacherCommunicationReports

import com.google.gson.annotations.SerializedName


class ReportReponse(@SerializedName("status") val status: String,
                    @SerializedName("data") val data: ReportDataModel,
                    @SerializedName("error_message") val message: String,
                    @SerializedName("error_code") val code: String)