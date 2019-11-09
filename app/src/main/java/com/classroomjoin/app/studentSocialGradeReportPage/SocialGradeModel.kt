package com.classroomjoin.app.studentSocialGradeReportPage

import com.classroomjoin.app.helper_utils.DisplayItem
import com.google.gson.annotations.SerializedName


data class SocialGradeModel(
        @SerializedName("positivePercentage")val positive:Float,
        @SerializedName("negativePercentage")val negative:Float,
        @SerializedName("totalGrade")val totalGrade:Float,
        @SerializedName("socailGradeStudentData")val data:List<SocialGradeStudentReport>): DisplayItem