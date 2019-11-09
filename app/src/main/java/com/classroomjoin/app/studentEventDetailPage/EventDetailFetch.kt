package com.classroomjoin.app.studentEventDetailPage


import com.classroomjoin.app.helper_utils.BaseEvent
import com.classroomjoin.app.helper_utils.Event
import com.classroomjoin.app.studentallEventsPage.StudentEventModel

class EventDetailFetch : BaseEvent {

    var studentDetail: StudentEventModel? = null

    var message: String = ""

    constructor(event: Event) : super(event) {}

    constructor(event: Event, studentDetail: StudentEventModel) : super(event) {
        this.studentDetail = studentDetail
    }

    constructor(event: Event, message: String) : super(event) {
        this.message = message
    }
}
