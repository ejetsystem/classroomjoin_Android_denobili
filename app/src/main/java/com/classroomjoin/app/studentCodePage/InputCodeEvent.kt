package com.classroomjoin.app.studentCodePage


import com.classroomjoin.app.helper_utils.BaseEvent
import com.classroomjoin.app.helper_utils.Event

class InputCodeEvent : BaseEvent {

    var studentDetail: InputCodeModel? = null

    var message: String? = null

    constructor(event: Event) : super(event) {}

    constructor(event: Event, studentDetail: InputCodeModel) : super(event) {
        this.studentDetail = studentDetail
    }

    constructor(event: Event, message: String?) : super(event) {
        this.message = message
    }
}
