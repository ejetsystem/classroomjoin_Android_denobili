package com.denobili.app.teacherSocialGradeReport

import com.google.gson.annotations.SerializedName


class SocialGradeReportSummaryResponse (@SerializedName("status")val status:String,
                                        @SerializedName("data")val data: ClassSummarySocialGrade,
                                        @SerializedName("error_code")val error_code:String,
                                        @SerializedName("error_message")val error_message:String)