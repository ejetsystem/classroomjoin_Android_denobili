package com.classroomjoin.app.studentDiaryEventDetailPage

import com.classroomjoin.app.studentDiaryPage.StudentDiaryEventModel

data class StudentDiaryEventApiResponse(val status:String, val error_status:String, val error_code:String, val data: StudentDiaryEventModel)