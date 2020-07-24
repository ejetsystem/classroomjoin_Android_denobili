package com.denobili.app.studentInboxPage

import com.denobili.app.studentDiaryPage.StudentDiaryEventModel
import com.denobili.app.studentallEventsPage.StudentEventModel
import com.google.gson.annotations.SerializedName


data class StudentInboxApiResponse(var status:String,
                                   var data: StudentEvent1Model,
                                   var error_code:String?=null,
                                   var error_status:String?=null)
data class StudentEvent1Model(
        var content: List<StudentEventModel>,
        var totalElements:Int,
        var totalPages:Int
        )