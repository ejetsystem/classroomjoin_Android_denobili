package com.denobili.app.studentallEventsPage


import com.denobili.app.helper_utils.DisplayItem
import com.denobili.app.helper_utils.Event
import com.denobili.app.helper_utils.ListEvent


class AllStudentEvent : ListEvent<DisplayItem> {

    constructor(event: Event) : super(event) {}

    constructor(event: Event, error: String) : super(event, error) {}

    //constructor(event: Event, results: List<DisplayItem>, error: String) : super(event, results, error) {}

    //constructor(error: String) : super(error) {}

    constructor(results: List<DisplayItem>) : super(results) {}


}
