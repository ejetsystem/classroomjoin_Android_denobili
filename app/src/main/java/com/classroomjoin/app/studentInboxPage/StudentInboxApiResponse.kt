package com.classroomjoin.app.studentInboxPage

import com.classroomjoin.app.studentallEventsPage.StudentEventModel


data class StudentInboxApiResponse(var status:String,
                                   var data:List<StudentEventModel>,
                                   var error_code:String?=null,
                                   var error_status:String?=null)