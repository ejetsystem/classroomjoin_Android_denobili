package com.denobili.app.readStatus

import com.denobili.app.helper_utils.Event
import com.denobili.app.helper_utils.ListEvent

class ReadStatusOTPEvent : ListEvent<ReadStatusReponser> {

    constructor(event: Event) : super(event) {}

    constructor(event: Event, error: String) : super(event, error) {}

}