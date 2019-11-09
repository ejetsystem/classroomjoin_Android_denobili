package com.classroomjoin.app.passwordConfirmation


import com.classroomjoin.app.helper_utils.DisplayItem
import com.classroomjoin.app.helper_utils.Event
import com.classroomjoin.app.helper_utils.ListEvent


class ConfirmPassEvent : ListEvent<DisplayItem> {

    constructor(event: Event) : super(event) {}

    constructor(event: Event, error: String) : super(event, error) {}

}
