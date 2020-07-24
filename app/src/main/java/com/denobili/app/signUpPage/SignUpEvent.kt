package com.denobili.app.signUpPage

import com.denobili.app.helper_utils.BaseEvent
import com.denobili.app.helper_utils.Event


class SignUpEvent : BaseEvent {

    var message: String? = null

    constructor(event: Event, message: String) : super(event) {
        this.message = message
    }

    constructor(event: Event) : super(event) {}
}
