package com.classroomjoin.app.studentDetailPage


import com.classroomjoin.app.helper_utils.BaseEvent
import com.classroomjoin.app.helper_utils.Event

class AddStudentEvent : BaseEvent {

    var studentDetail: StudentDetail? = null

    var message: String? = null

    constructor(event: Event) : super(event) {}

    constructor(event: Event, studentDetail: StudentDetail?) : super(event) {
        this.studentDetail = studentDetail
    }

    constructor(event: Event, message: String?) : super(event) {
        this.message = message
    }
}
