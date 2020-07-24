package com.denobili.app.signUpPage.getOTPsignup

import com.denobili.app.helper_utils.Event
import com.denobili.app.helper_utils.ListEvent

class SignupOTPEvent : ListEvent<SignupOTPDataReponser> {

    constructor(event: Event) : super(event) {}

    constructor(event: Event, error: String) : super(event, error) {}

}