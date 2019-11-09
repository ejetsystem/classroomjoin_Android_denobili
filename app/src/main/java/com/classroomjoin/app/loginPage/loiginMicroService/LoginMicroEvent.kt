package com.classroomjoin.app.loginPage.loiginMicroService

import com.classroomjoin.app.helper_utils.Event
import com.classroomjoin.app.helper_utils.ListEvent

class LoginMicroEvent : ListEvent<LoginMicroDataReponser> {

    constructor(event: Event) : super(event) {}

    constructor(event: Event, error: String) : super(event, error) {}

}