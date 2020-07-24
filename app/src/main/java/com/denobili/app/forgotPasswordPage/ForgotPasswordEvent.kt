package com.denobili.app.forgotPasswordPage


import com.denobili.app.helper_utils.DisplayItem
import com.denobili.app.helper_utils.Event
import com.denobili.app.helper_utils.ListEvent


class ForgotPasswordEvent : ListEvent<DisplayItem> {

    constructor(event: Event) : super(event)

    constructor(event: Event, error: String) : super(event, error)


}
