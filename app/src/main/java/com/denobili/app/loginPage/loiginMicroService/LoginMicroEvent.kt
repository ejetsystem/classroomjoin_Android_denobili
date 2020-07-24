package com.denobili.app.loginPage.loiginMicroService

import com.denobili.app.helper_utils.Event
import com.denobili.app.helper_utils.ListEvent

class LoginMicroEvent : ListEvent<LoginMicroDataReponser> {

    constructor(event: Event) : super(event) {}

    constructor(event: Event, error: String) : super(event, error) {}

}