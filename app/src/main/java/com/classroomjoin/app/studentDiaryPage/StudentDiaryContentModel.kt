package com.classroomjoin.app.studentDiaryPage

import com.google.gson.annotations.SerializedName


class StudentDiaryContentModel(
        @SerializedName("content")
        val content: List<StudentDiaryEventModel>)