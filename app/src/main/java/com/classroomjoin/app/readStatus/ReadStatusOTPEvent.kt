package com.classroomjoin.app.readStatus

import com.classroomjoin.app.helper_utils.Event
import com.classroomjoin.app.helper_utils.ListEvent

class ReadStatusOTPEvent : ListEvent<ReadStatusReponser> {

    constructor(event: Event) : super(event) {}

    constructor(event: Event, error: String) : super(event, error) {}

}