package com.denobili.app.loginPage.signupGoogle

import com.denobili.app.helper_utils.Event
import com.denobili.app.helper_utils.ListEvent

class SignInGoogleEvent : ListEvent<SignInGoogleDataReponser> {

    constructor(event: Event) : super(event) {}

    constructor(event: Event, error: String) : super(event, error) {}

}