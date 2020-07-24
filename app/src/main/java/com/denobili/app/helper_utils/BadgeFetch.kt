package com.denobili.app.helper_utils

import com.denobili.app.studentallEventsPage.StudentEventModel

class BadgeFetch : BaseEvent{
    var studentDetail: BadgeCountModel? = null

    var message: String = ""

    constructor(event: Event) : super(event) {}

    constructor(event: Event, studentDetail: BadgeCountModel) : super(event) {
        this.studentDetail = studentDetail
    }

    constructor(event: Event, message: String) : super(event) {
        this.message = message
    }
}