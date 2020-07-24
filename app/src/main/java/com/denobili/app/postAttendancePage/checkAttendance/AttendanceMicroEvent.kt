package com.denobili.app.postAttendancePage.checkAttendance

import com.denobili.app.helper_utils.Event
import com.denobili.app.helper_utils.ListEvent

class AttendanceMicroEvent : ListEvent<AttendanceMicroDataReponser> {

    constructor(event: Event) : super(event) {}

    constructor(event: Event, error: String) : super(event, error) {}

}