package com.classroomjoin.app.teacherSocialGradeReport

import com.classroomjoin.app.helper_utils.DisplayItem
import com.google.gson.annotations.SerializedName


data class StudentSummarySocialGrade(@SerializedName("studentName")val name:String,
                                     @SerializedName("positivePercentage")val positive:Float,
                                     @SerializedName("negativePercentage")val negative:Float): DisplayItem