package com.denobili.app.teacherSocialGradeReport

import com.denobili.app.helper_utils.DisplayItem
import com.google.gson.annotations.SerializedName


data class StudentSummarySocialGrade(@SerializedName("studentName")val name:String,
                                     @SerializedName("positivePercentage")val positive:Float,
                                     @SerializedName("negativePercentage")val negative:Float): DisplayItem