package com.denobili.app.studentDiaryEventDetailPage

import com.denobili.app.studentDiaryPage.StudentDiaryEventModel

data class StudentDiaryEventApiResponse(val status:String, val error_status:String, val error_code:String, val data: StudentDiaryEventModel)