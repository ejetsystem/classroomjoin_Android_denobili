package com.denobili.app.teacherSocialGradeReport

import com.denobili.app.helper_utils.DisplayItem
import com.google.gson.annotations.SerializedName


class ClassSummarySocialGrade(@SerializedName("classId")val class_id:Int,
                              @SerializedName(value="positivePercentCal", alternate= arrayOf("positivepercentage")) val positive:Float,
                              @SerializedName(value="negativePercentCal", alternate= arrayOf("negativepercentage"))val negative:Float,
                              @SerializedName(value="socailGradeStudentData", alternate= arrayOf("socailGradeStudentDataPercent"))val data: List<StudentSummarySocialGrade>): DisplayItem