package com.denobili.app.studentEventDetailPage


import com.denobili.app.helper_utils.BaseEvent
import com.denobili.app.helper_utils.Event
import com.denobili.app.studentallEventsPage.StudentEventModel

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
