package com.denobili.app.studentSocialGradeReportPage

import com.denobili.app.helper_utils.DisplayItem
import com.google.gson.annotations.SerializedName


data class SocialGradeStudentReport(
        @SerializedName("grade")val grade:String,
        @SerializedName("teacherName")val teachername:String,
        @SerializedName("createDate")val date:String,
        @SerializedName("socialGradeName")val name:String,
        @SerializedName("flag")val flag:String): DisplayItem