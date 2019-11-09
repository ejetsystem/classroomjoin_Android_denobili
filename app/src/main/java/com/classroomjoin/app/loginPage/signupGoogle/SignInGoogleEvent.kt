package com.classroomjoin.app.loginPage.signupGoogle

import com.classroomjoin.app.helper_utils.Event
import com.classroomjoin.app.helper_utils.ListEvent

class SignInGoogleEvent : ListEvent<SignInGoogleDataReponser> {

    constructor(event: Event) : super(event) {}

    constructor(event: Event, error: String) : super(event, error) {}

}