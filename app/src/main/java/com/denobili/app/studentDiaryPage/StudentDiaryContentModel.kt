package com.denobili.app.studentDiaryPage

import com.google.gson.annotations.SerializedName


class StudentDiaryContentModel(
        @SerializedName("content")
        var content: List<StudentDiaryEventModel>,
        @SerializedName("totalPages")
val totalPages:Int,
        @SerializedName("totalElements")
        val totalElements:Int)