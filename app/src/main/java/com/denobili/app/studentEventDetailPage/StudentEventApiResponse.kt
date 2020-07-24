package com.denobili.app.studentEventDetailPage

import com.denobili.app.studentallEventsPage.StudentEventModel


data class StudentEventApiResponse(val status:String,val error_status:String,val error_code:String,val data: StudentEventModel)