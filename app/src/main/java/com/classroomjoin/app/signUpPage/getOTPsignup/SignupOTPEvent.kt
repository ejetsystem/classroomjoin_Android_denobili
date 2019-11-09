package com.classroomjoin.app.signUpPage.getOTPsignup

import com.classroomjoin.app.helper_utils.Event
import com.classroomjoin.app.helper_utils.ListEvent

class SignupOTPEvent : ListEvent<SignupOTPDataReponser> {

    constructor(event: Event) : super(event) {}

    constructor(event: Event, error: String) : super(event, error) {}

}