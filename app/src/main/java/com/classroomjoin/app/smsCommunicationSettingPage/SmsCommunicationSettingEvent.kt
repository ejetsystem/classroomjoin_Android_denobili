package com.classroomjoin.app.smsCommunicationSettingPage

import com.classroomjoin.app.emailSettingPage.CommunicationModel
import com.classroomjoin.app.emailSettingPage.CommunicationSettingModel
import com.classroomjoin.app.helper_utils.BaseEvent
import com.classroomjoin.app.helper_utils.Event


class SmsCommunicationSettingEvent(event: Event): BaseEvent(event) {

    var model: CommunicationSettingModel?=null
    var error:String?=null
    var result: List<CommunicationModel>?=null

    constructor(event: Event, model: CommunicationSettingModel):this(event){
        this.model=model
    }

    constructor(event: Event, model: List<CommunicationModel>):this(event){
        this.result=model
    }

    constructor(event: Event, error: String):this(event){
        this.error=error
    }
}